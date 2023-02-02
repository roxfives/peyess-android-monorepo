package com.peyess.salesapp.workmanager.picture_upload

import android.content.Context
import android.webkit.MimeTypeMap
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.flatMap
import com.google.firebase.storage.ktx.storageMetadata
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.utils.cloud.storage.uploadPicture
import com.peyess.salesapp.workmanager.picture_upload.error.ErrorType
import com.peyess.salesapp.workmanager.picture_upload.error.LocalOperationFailed
import com.peyess.salesapp.workmanager.picture_upload.error.PictureUploadManagerError
import com.peyess.salesapp.workmanager.picture_upload.error.StorageNotInitialized
import com.peyess.salesapp.workmanager.picture_upload.error.UploadFailed
import com.peyess.salesapp.workmanager.picture_upload.typing.UploadCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private typealias PictureUploadManagerResponse = Either<PictureUploadManagerError, Unit>

const val uploadEntryIdKey = "uploadEntryIdKey"
const val tooManyAttemptsThreshold = 20

@HiltWorker
class PictureUploadManagerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesApplication: SalesApplication,
    private val pictureUploadRepository: PictureUploadRepository,
    private val firebaseManager: FirebaseManager,
): CoroutineWorker(context, workerParams) {
    private fun checkUploadPictureScenario(
        pictureDocument: PictureUploadDocument,
    ): UploadCase {
        return if (pictureDocument.hasBeenUploaded) {
            UploadCase.AlreadyUploaded
        } else if (pictureDocument.attemptCount > tooManyAttemptsThreshold) {
            UploadCase.UploadFailedTooManyTimes
        } else if (pictureDocument.attemptCount > 0) {
            UploadCase.UploadFailedPreviously
        } else {
            UploadCase.UploadPending
        }
    }

    private suspend fun uploadPicture(
        pictureUpload: PictureUploadDocument,
    ): PictureUploadManagerResponse = either {
        Timber.i("Picture with id ${pictureUpload.id} pending upload")

        val storageRef = try {
            firebaseManager.storage
        } catch (err: Throwable) {
            Timber.e("Failed to get storage reference", err)
            null
        }

        ensureNotNull(storageRef) {
            StorageNotInitialized(description = "Reference to storage is null")
        }

        val context = salesApplication as Context
        val contentResolver = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        val type = mime.getExtensionFromMimeType(
            contentResolver.getType(pictureUpload.picture)
        ).let {
            if (it == "jpg") { "jpeg" } else { it }
        }

        val storageMetadata = storageMetadata {
            contentType = "image/$type"
        }

        uploadPicture(
            storage = storageRef,
            storagePath = pictureUpload.storagePath,
            filename = pictureUpload.storageName,
            picture = pictureUpload.picture,
            metadata = storageMetadata,
            onProgressUpdate = {},
        ).mapLeft {
            UploadFailed(
                errorType = ErrorType.UploadError(it),
                description = "Failed to upload picture with id " +
                        "${pictureUpload.id} and uri ${pictureUpload.picture}",
                error = it.error,
            )
        }.bind()
    }

    private suspend fun increaseAttemptCount(
        pictureUpload: PictureUploadDocument,
    ): Either<PictureUploadManagerError, Unit> {
        val update = pictureUpload.copy(
            attemptCount = pictureUpload.attemptCount + 1,
        )

        return pictureUploadRepository.updatePicture(update).mapLeft {
            LocalOperationFailed(
                errorType = ErrorType.LocalError(it),
                description = "Failed to increase attempt of picture with id ${pictureUpload.id}",
                error = it.error,
            )
        }
    }

    private suspend fun markAsFinished(
        pictureUpload: PictureUploadDocument,
    ): PictureUploadManagerResponse {
        val update = pictureUpload.copy(
            hasBeenUploaded = true,
        )

        return pictureUploadRepository.updatePicture(update).mapLeft {
            LocalOperationFailed(
                errorType = ErrorType.LocalError(it),
                description = "Failed to mark picture with id ${pictureUpload.id} as uploaded",
                error = it.error,
            )
        }
    }

    private suspend fun uploadPendingWork(
        pictureUpload: PictureUploadDocument,
    ): PictureUploadManagerResponse = either {
        Timber.i("Picture with id ${pictureUpload.id} pending upload")

        increaseAttemptCount(pictureUpload).bind()
        uploadPicture(pictureUpload).bind()
        markAsFinished(pictureUpload).bind()
    }

    private suspend fun uploadFailedTooMuchWork(
        pictureUpload: PictureUploadDocument,
    ): PictureUploadManagerResponse = either {
        Timber.i("Picture with id ${pictureUpload.id} failed too much, " +
                "attempt count: ${pictureUpload.attemptCount}")

        uploadPendingWork(pictureUpload).bind()
    }

    override suspend fun doWork(): Result {
        val entryId = inputData.getLong(uploadEntryIdKey, 0L)

        return pictureUploadRepository
            .pictureById(entryId)
            .mapLeft {
                LocalOperationFailed(
                    errorType = ErrorType.LocalError(it),
                    description = "Failed to retrieve picture with id $entryId",
                    error = it.error,
                )
            }.flatMap {
                when (checkUploadPictureScenario(it)) {
                    UploadCase.UploadFailedPreviously -> uploadPendingWork(it)
                    UploadCase.UploadFailedTooManyTimes -> uploadFailedTooMuchWork(it)
                    UploadCase.UploadPending -> uploadPendingWork(it)
                    UploadCase.AlreadyUploaded -> Either.Right(Unit) as PictureUploadManagerResponse
                }
            }.fold(
                ifLeft = {
                    Timber.e(
                        message = "Failed to upload picture with id $entryId: ${it.description}",
                        t = it.error,
                    )

                    Result.retry()
                },

                ifRight = {
                    Timber.i("Picture with id $entryId uploaded successfully")
                    Result.success()
                },
            )
    }

    companion object {
        const val workerTag = "TAG_PictureUploadManagerWorker"
    }
}
