package com.peyess.salesapp.dao.client.firestore

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ClientDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ClientDao {
    override fun clients(): Flow<List<ClientDocument>> = callbackFlow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@callbackFlow
        }

        firestore.collection(salesApplication.stringResource(R.string.fs_col_clients))
            .orderBy(salesApplication.stringResource(R.string.fs_field_clients_name))
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Timber.e(exception, "Failed while fetching clients")
                }

                val clients = snapshot?.mapNotNull {
                    try {
                        it.toObject(FSClient::class.java).toDocument(it.id)
                    } catch (error: Throwable) {
                        Timber.e(error, "Failed to convert client document")
                        null
                    }
                }

                Timber.i("Sending clients $clients")
                trySend(clients ?: emptyList())
            }

        awaitClose()
//            .get()
//            .addOnCompleteListener {
//                Timber.i("Clients task: ${it.isSuccessful}")
//
//                if (!it.isSuccessful) {
//                    Timber.e(it.exception)
//                }
//            }
//            .await()

//        Timber.i("Mapping clients from: ${snaps}")
//        val clients = snaps.mapNotNull {
//                try {
//                    it.toObject(FSClient::class.java).toDocument(it.id)
//                } catch (error: Throwable) {
//                    Timber.e(error, "Failed to convert client document")
//                    null
//                }
//            }
//
//        emit(clients)
    }

    override fun clientById(clientId: String): Flow<ClientDocument?> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snap = firestore
            .collection(
                salesApplication.stringResource(R.string.fs_col_clients)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .document(clientId)
            .get()
            .await()

        if (!snap.exists()) {
            emit(null)
            return@flow
        }

        val client = snap.toObject(FSClient::class.java)?.toDocument(snap.id)

        emit(client)
        return@flow
    }

    override suspend fun addClient(clientId: String, client: FSClient) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        try {
            firestore
                .document(
                    salesApplication
                        .stringResource(R.string.fs_doc_client)
                        .format(clientId)
                )
                .set(client)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }
    }
}