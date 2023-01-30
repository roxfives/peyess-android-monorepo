package com.peyess.salesapp.data.dao.client_legal

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.client_legal.FSClientLegal
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientLegalDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ClientLegalDao {
    override suspend fun addClientLegalFor(clientId: String, clientLegal: FSClientLegal) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        try {
            firestore.document(
                salesApplication
                        .stringResource(R.string.fs_doc_client_legal)
                        .format(clientId)
                )
                .set(clientLegal)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }
    }
}