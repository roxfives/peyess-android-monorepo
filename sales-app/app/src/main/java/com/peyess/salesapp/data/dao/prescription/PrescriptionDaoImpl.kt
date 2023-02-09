package com.peyess.salesapp.data.dao.prescription

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.leftIfNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.prescription.error.ReadPrescriptionDaoError
import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PrescriptionDaoImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val salesApplication: SalesApplication,
): PrescriptionDao {
    override suspend fun add(document: FSPrescription) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        val storeId = firebaseManager.currentStore?.uid
        val prescriptionCollectionPath = salesApplication
            .stringResource(R.string.fs_col_prescription)
            .format(storeId)

        try {
            firestore.collection(prescriptionCollectionPath)
                .document(document.id)
                .set(document)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client", err)
            error(err)
        }
    }

    override suspend fun prescriptionById(
        prescriptionId: String
    ): ReadPrescriptionDaoResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ReadPrescriptionDaoError.Unexpected(
                description = "Firestore instance is uninitialized"
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            ReadPrescriptionDaoError.Unexpected(
                description = "Store is not authenticated"
            )
        }

        val prescriptionCollectionPath = salesApplication
            .stringResource(R.string.fs_col_prescription)
            .format(storeId)

        val result = Either.catch {
            firestore.collection(prescriptionCollectionPath)
                .document(prescriptionId)
                .get()
                .await()
        }.mapLeft {
            ReadPrescriptionDaoError.Unexpected(
                description = it.message ?: "Unknown error",
                throwable = it,
            )
        }.leftIfNull {
            ReadPrescriptionDaoError.NotFound(
                description = "Prescription not found"
            )
        }.bind()

        val response = result.toObject(FSPrescription::class.java)
        ensureNotNull(response) {
            ReadPrescriptionDaoError.Unexpected(
                description = "Prescription not found"
            )
        }

        response
    }
}
