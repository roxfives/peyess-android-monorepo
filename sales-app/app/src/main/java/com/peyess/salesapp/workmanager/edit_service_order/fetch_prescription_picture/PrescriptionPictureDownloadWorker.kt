package com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.positioning.error.ReadPositioningError
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.error.UpdatePrescriptionError
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.data.repository.prescription.error.GetPictureUrlPrescriptionRepositoryError
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.error.FetchPictureUriError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.error.FetchPrescriptionError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.error.PrescriptionPictureDownloadWorkerError
import com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.error.UpdatePictureUriError
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

private typealias FetchPrescriptionResponse =
        Either<PrescriptionPictureDownloadWorkerError, PrescriptionDocument>
private typealias FetchPictureUriResponse = Either<FetchPictureUriError, Uri>
private typealias UpdatePrescriptionResponse = Either<UpdatePictureUriError, Unit>

const val prescriptionIdEntryKey = "prescriptionIdEntryKey"

@HiltWorker
class PrescriptionPictureDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val prescriptionRepository: PrescriptionRepository,
    private val editPrescriptionRepository: EditPrescriptionRepository,
): CoroutineWorker(context, workerParams) {

    private suspend fun fetchPrescription(
        prescriptionId: String,
    ): FetchPrescriptionResponse {
        return prescriptionRepository.prescriptionById(prescriptionId).mapLeft { err ->
            when (err) {
                is ReadPrescriptionRepositoryError.NotFound ->
                    FetchPrescriptionError.NotFound(
                        description = err.description,
                        throwable = err.error,
                    )
                is ReadPrescriptionRepositoryError.Unexpected ->
                    FetchPrescriptionError.Unexpected(
                        description = err.description,
                        throwable = err.error,
                    )
            }
        }
    }

    private suspend fun fetchPictureUri(
        prescription: PrescriptionDocument,
    ): FetchPictureUriResponse {
        return prescriptionRepository.getPictureUri(prescription).mapLeft { err ->
            when (err) {
                is GetPictureUrlPrescriptionRepositoryError.NotFound ->
                    FetchPictureUriError.NotFound(
                        description = err.description,
                        throwable = err.error,
                    )
                is GetPictureUrlPrescriptionRepositoryError.Unexpected ->
                    FetchPictureUriError.Unexpected(
                        description = err.description,
                        throwable = err.error,
                    )
            }
        }
    }

    private suspend fun updatePrescriptionUri(
        prescriptionId: String,
        pictureUri: Uri,
    ): UpdatePrescriptionResponse = either {
        val currentPrescription = editPrescriptionRepository
            .prescriptionById(prescriptionId)
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

        if (currentPrescription.pictureUri != Uri.EMPTY) {
            Timber.i("updatePrescriptionUri: prescription $prescriptionId already has a picture")
            return@either
        } else {
            editPrescriptionRepository.updatePicture(prescriptionId, pictureUri).mapLeft { err ->
                when (err) {
                    is UpdatePrescriptionError.Unexpected ->
                        UpdatePictureUriError.Unexpected(
                            description = err.description,
                            throwable = err.error,
                        )
                }
            }.bind()
        }
    }

    private fun decideRetry(response: PrescriptionPictureDownloadWorkerError): Result {
        return when (response) {
            is FetchPrescriptionError.NotFound,
            is FetchPictureUriError.NotFound,
            is UpdatePictureUriError.NotFound -> Result.failure()
            is FetchPrescriptionError.Unexpected,
            is FetchPictureUriError.Unexpected,
            is UpdatePictureUriError.Unexpected -> Result.retry()
        }
    }

    override suspend fun doWork(): Result {
        val prescriptionId = inputData.getString(prescriptionIdEntryKey) ?: ""

        val response = fetchPrescription(prescriptionId)
            .flatMap { prescription ->
                Timber.i("doWork: fetched prescription: $prescription")
                fetchPictureUri(prescription).flatMap { pictureUri ->
                    Timber.i("doWork: updating prescription " +
                            "$prescriptionId with pictureUri: $pictureUri")

                    updatePrescriptionUri(prescriptionId, pictureUri)
                }
            }

        return response.fold(
            ifLeft = { decideRetry(it) },
            ifRight = { Result.success() },
        )
    }
}
