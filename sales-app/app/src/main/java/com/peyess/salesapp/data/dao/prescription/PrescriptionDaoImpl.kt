package com.peyess.salesapp.data.dao.prescription

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
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
}