package com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.UpdatePositioningError
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.positioning.error.GetPictureUriPositioningRepositoryError
import com.peyess.salesapp.data.repository.positioning.error.ReadPositioningRepositoryError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.error.FetchPictureUriError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.error.FetchPositioningError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.error.PositioningPictureDownloadWorkerError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.error.UpdatePictureUriError
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private typealias FetchPositioningResponse =
        Either<PositioningPictureDownloadWorkerError, PositioningDocument>
private typealias FetchPictureUriResponse = Either<FetchPictureUriError, Uri>
private typealias UpdatePositioningResponse = Either<UpdatePictureUriError, Unit>

const val positioningIdEntryKey = "positioningIdEntryKey"

@HiltWorker
class PositioningPictureDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val positioningRepository: PositioningRepository,
    private val editPositioningRepository: EditPositioningRepository,
): CoroutineWorker(context, workerParams) {

    private suspend fun fetchPositioning(
        positioningId: String,
    ): FetchPositioningResponse {
        return positioningRepository.positioningById(positioningId).mapLeft { err ->
            when (err) {
                is ReadPositioningRepositoryError.NotFound ->
                    FetchPositioningError.NotFound(
                        description = err.description,
                        throwable = err.error,
                    )
                is ReadPositioningRepositoryError.Unexpected ->
                    FetchPositioningError.Unexpected(
                        description = err.description,
                        throwable = err.error,
                    )
            }
        }
    }

    private suspend fun fetchPictureUri(
        positioning: PositioningDocument,
    ): FetchPictureUriResponse {
        return positioningRepository.getPictureUri(positioning).mapLeft { err ->
            when (err) {
                is GetPictureUriPositioningRepositoryError.NotFound ->
                    FetchPictureUriError.NotFound(
                        description = err.description,
                        throwable = err.error,
                    )
                is GetPictureUriPositioningRepositoryError.Unexpected ->
                    FetchPictureUriError.Unexpected(
                        description = err.description,
                        throwable = err.error,
                    )
            }
        }
    }

    private suspend fun updatePositioningUri(
        positioningId: String,
        pictureUri: Uri,
    ): UpdatePositioningResponse = either {
        val currentPositioning = editPositioningRepository
            .positioningById(positioningId)
            .mapLeft {
                when (it) {
                    is ReadPositioningError.PositioningNotFound ->
                        UpdatePictureUriError.NotFound(
                            description = it.description,
                            throwable = it.error,
                        )
                    is ReadPositioningError.Unexpected ->
                        UpdatePictureUriError.Unexpected(
                            description = it.description,
                            throwable = it.error,
                        )
                }
            }.bind()

        if (currentPositioning.picture != Uri.EMPTY) {
            Timber.i("updatePositioningUri: positioning $positioningId already has a picture")
            return@either
        } else {
            editPositioningRepository.updatePictureById(positioningId, pictureUri).mapLeft { err ->
                when (err) {
                    is UpdatePositioningError.Unexpected ->
                        UpdatePictureUriError.Unexpected(
                            description = err.description,
                            throwable = err.error,
                        )
                }
            }.bind()
        }
    }

    private fun decideRetry(response: PositioningPictureDownloadWorkerError): Result {
        return when (response) {
            is FetchPositioningError.NotFound,
            is FetchPictureUriError.NotFound,
            is UpdatePictureUriError.NotFound -> Result.failure()
            is FetchPositioningError.Unexpected,
            is FetchPictureUriError.Unexpected,
            is UpdatePictureUriError.Unexpected -> Result.retry()
        }
    }

    override suspend fun doWork(): Result {
        val positioningId = inputData.getString(positioningIdEntryKey) ?: ""

        val response = fetchPositioning(positioningId)
            .flatMap { positioning ->
                Timber.i("doWork: fetched positioning: $positioning")
                fetchPictureUri(positioning).flatMap { pictureUri ->
                    Timber.i("doWork: updating positioning " +
                            "$positioningId with pictureUri: $pictureUri")

                    updatePositioningUri(positioningId, pictureUri)
                }
            }

        return response.fold(
            ifLeft = { decideRetry(it) },
            ifRight = { Result.success() },
        )
    }
}
