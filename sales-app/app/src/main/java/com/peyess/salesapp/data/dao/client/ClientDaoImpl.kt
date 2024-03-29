package com.peyess.salesapp.data.dao.client

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.client.toMap
import com.peyess.salesapp.data.dao.client.error.ExistsClientDaoError
import com.peyess.salesapp.data.dao.client.error.ReadClientDaoError
import com.peyess.salesapp.data.dao.client.error.UpdateClientDaoError
import com.peyess.salesapp.data.dao.client.utils.ClientsCollectionPaginator
import com.peyess.salesapp.data.dao.internal.firestore.FetchCollectionResponse
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.Unexpected
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.model.client.toDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ClientDao {
    override fun clients(): Flow<List<ClientDocument>> = callbackFlow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@callbackFlow
        }

        // Filter by store access
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
    }

    override suspend fun clientById(clientId: String): ClientDocument? {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return null
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
            return null
        }

        return snap.toObject(FSClient::class.java)
            ?.toDocument(snap.id)
    }

    override suspend fun clientByDocument(document: String): ReadClientResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ReadClientDaoError.UnexpectedError(
                description = "Firestore instance is null",
            )
        }

        val snap = Either.catch {
            firestore
                .collection(
                    salesApplication.stringResource(R.string.fs_col_clients)
                        .format(firebaseManager.currentStore!!.uid)
                )
                .whereEqualTo("document", document)
                .get()
                .await()
        }.mapLeft {
            ReadClientDaoError.UnexpectedError(
                description = "Error while fetching client by document",
                throwable = it,
            )
        }.bind()

        ensure(snap.documents.isNotEmpty()) {
            ReadClientDaoError.ClientNotFound(
                description = "Client with document $document not found",
            )
        }

        val snapDocument = snap.documents.first()
        val result = snapDocument.toObject(FSClient::class.java)
        ensureNotNull(result) {
            ReadClientDaoError.UnexpectedError(
                description = "Error while converting client document",
            )
        }

        Pair(snapDocument.id, result)
    }

    override suspend fun clientExistsById(
        clientId: String,
    ): ExistsClientIdResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ExistsClientDaoError.UnexpectedError(
                description = "Firestore instance is null",
            )
        }

        val documentPath = salesApplication.stringResource(R.string.fs_doc_client).format(clientId)
        val snap = Either.catch {
            firestore.document(documentPath).get().await()
        }.mapLeft {
            ExistsClientDaoError.UnexpectedError(
                description = "Error while fetching client by id",
                throwable = it,
            )
        }.bind()

        snap.exists()
    }

    override suspend fun clientExistsByDocument(
        document: String,
    ): ExistsClientDocumentResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ExistsClientDaoError.UnexpectedError(
                description = "Firestore instance is null",
            )
        }

        val collectionPath = salesApplication.stringResource(R.string.fs_col_clients)
        val documentField = salesApplication.stringResource(R.string.fs_field_clients_document)
        val querySnap = Either.catch {
            firestore.collection(collectionPath)
                .whereEqualTo(documentField, document)
                .get()
                .await()
        }.mapLeft {
            ExistsClientDaoError.UnexpectedError(
                description = "Error while fetching client by id",
                throwable = it,
            )
        }.bind()

        if (querySnap.isEmpty) {
            ""
        } else {
            querySnap.documents.first().id
        }
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
            Timber.e("Error while adding client, ${err.cause}", err)
            error(err)
        }
    }

    override suspend fun updateClient(
        clientId: String,
        client: FSClient,
    ): UpdateClientResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            UpdateClientDaoError.UnexpectedError("Firestore instance is null")
        }

        val documentPath = salesApplication
            .stringResource(R.string.fs_doc_client)
            .format(clientId)
        Either.catch {
            firestore.document(documentPath)
                .update(client.toMap())
                .await()
        }.mapLeft {
            UpdateClientDaoError.UnexpectedError(
                description = "Error while updating client",
                throwable = it,
            )
        }.bind()
    }

    override suspend fun getById(id: String): Either<FirestoreError, FSClient> = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            Unexpected("Firestore instance is null", null)
        }

        val localLensPath = salesApplication
            .stringResource(R.string.fs_doc_client)
            .format(id)
        val docRef = firestore.document(localLensPath)

        fetchDocument(FSClient::class, docRef).bind()
    }

    override suspend fun simpleCollectionPaginator(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): Either<Unexpected, SimpleCollectionPaginator<FSClient>> = either {
        val firestore = firebaseManager.storeFirestore

        ensureNotNull(firestore) {
            Unexpected("Firestore instance is null", null)
        }

        val path = salesApplication
            .stringResource(R.string.fs_col_clients)

        ClientsCollectionPaginator(
            query = query.toFirestoreCollectionQuery(
                path = path,
                firestore = firestore,
            ),
            config = SimplePaginatorConfig(
                initialPageSize = config.initialPageSize,
                pageSize = config.pageSize,
            ),
        )
    }

    override suspend fun fetchCollection(query: PeyessQuery): FetchCollectionResponse<FSClient> {
        TODO("Not yet implemented")
    }
}