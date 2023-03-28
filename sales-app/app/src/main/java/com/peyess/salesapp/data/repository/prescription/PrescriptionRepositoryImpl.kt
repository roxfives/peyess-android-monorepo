package com.peyess.salesapp.data.repository.prescription

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.leftIfNull
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.prescription.toFSPrescription
import com.peyess.salesapp.data.adapter.prescription.toFSPrescriptionUpdate
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.prescription.error.ReadPrescriptionDaoError
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import com.peyess.salesapp.data.repository.prescription.adapter.toPrescriptionDocument
import com.peyess.salesapp.data.repository.prescription.error.GetPictureUrlPrescriptionRepositoryError
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError
import com.peyess.salesapp.data.repository.prescription.error.UpdatePrescriptionRepositoryError
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.utils.cloud.storage.appendStoragePaths
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PrescriptionRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val prescriptionDao: PrescriptionDao,
): PrescriptionRepository {
    override suspend fun add(prescription: PrescriptionDocument) {
        val fsPositioning = prescription.toFSPrescription()

        prescriptionDao.add(fsPositioning)
    }

    override suspend fun prescriptionById(
        prescriptionId: String,
    ): ReadPrescriptionRepositoryResponse = either {
        val response = prescriptionDao
            .prescriptionById(prescriptionId)
            .mapLeft {
                when(it) {
                    is ReadPrescriptionDaoError.NotFound ->
                        ReadPrescriptionRepositoryError.NotFound(
                            description = it.description,
                            throwable = it.throwable
                        )
                    is ReadPrescriptionDaoError.Unexpected ->
                        ReadPrescriptionRepositoryError.Unexpected(
                            description = it.description,
                            throwable = it.throwable
                        )
                }
            }.bind()

        response.toPrescriptionDocument()
    }

    override suspend fun updatePrescription(
        prescriptionId: String,
        prescription: PrescriptionUpdateDocument,
    ): UpdatePrescriptionResponse = either {
        prescriptionDao.updatePrescription(prescriptionId, prescription.toFSPrescriptionUpdate())
            .mapLeft {
                UpdatePrescriptionRepositoryError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()
    }

    override suspend fun getPictureUri(
        prescription: PrescriptionDocument,
    ): GetPictureUrlPrescriptionRepositoryResponse {
        val storageRef = firebaseManager.storage?.reference
            ?: FirebaseStorage.getInstance().reference

        val storagePicturePath = salesApplication
            .stringResource(R.string.storage_client_prescription)
            .format(
                prescription.storeId,
                prescription.patientUid,
                prescription.id,
            )
        val filename = salesApplication.stringResource(R.string.storage_client_prescription_filename)

        val storagePath = appendStoragePaths(listOf(storagePicturePath, filename))

        return Either.catch {
            storageRef.child(storagePath)
                .downloadUrl
                .await()
        }.mapLeft {
            Timber.e("Failed to download prescription at $storagePath", it)

            GetPictureUrlPrescriptionRepositoryError.Unexpected(
                description = "Failed to download prescription at $storagePath",
                throwable = it,
            )
        }.leftIfNull {
            Timber.e("Prescription picture at $storagePicturePath does not exist")

            GetPictureUrlPrescriptionRepositoryError.NotFound(
                description = "Failed to download prescription at $storagePicturePath",
            )
        }
    }
}
