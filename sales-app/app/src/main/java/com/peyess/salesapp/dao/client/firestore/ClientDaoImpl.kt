package com.peyess.salesapp.dao.client.firestore

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ClientDao {
    override fun clients(): Flow<List<Client>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(
                salesApplication.stringResource(R.string.fs_col_clients)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .orderBy(salesApplication.stringResource(R.string.fs_field_clients_name))
            .get()
            .await()

        val clients = snaps.mapNotNull {
                it.toObject(FSClient::class.java).toDocument()
            }

        emit(clients)
    }

    override fun addClient(client: Client): Flow<Boolean> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val docId = firebaseManager.uniqueId()
        val clientData = client.copy(id = docId).toFirestore()

        try {
            firestore
                .collection(
                    salesApplication.stringResource(R.string.fs_col_clients)
                        .format(firebaseManager.currentStore!!.uid)
                )
                .document(docId)
                .set(clientData)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")

            error(err)
        }

        emit(true)
        return@flow
    }
}