package com.peyess.salesapp.data.repository.client

import android.net.Uri
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.leftIfNull
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.client.ClientDao
import com.peyess.salesapp.data.adapter.client.toCacheCreateClientEntity
import com.peyess.salesapp.data.adapter.client.toFSClient
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.client_legal.ClientLegalDao
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.model.client.toDocument
import com.peyess.salesapp.data.model.client_legal.ClientLegalMethod
import com.peyess.salesapp.data.model.client_legal.FSClientLegal
import com.peyess.salesapp.data.repository.client.error.ClientNotFound
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryUnexpectedError
import com.peyess.salesapp.data.repository.internal.firestore.errors.CreatePaginatorError
import com.peyess.salesapp.data.repository.internal.firestore.errors.FetchDataError
import com.peyess.salesapp.data.repository.internal.firestore.errors.FetchPageError
import com.peyess.salesapp.data.repository.internal.firestore.errors.ReadError
import com.peyess.salesapp.data.repository.internal.firestore.errors.Unexpected
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val cacheCreateClientDao: CacheCreateClientDao,
    private val clientDao: ClientDao,
    private val clientLegalDao: ClientLegalDao,
    private val authenticationRepository: AuthenticationRepository,
): ClientRepository {
    private var paginator: SimpleCollectionPaginator<FSClient>? = null

    override fun clients(): Flow<List<ClientDocument>> {
        return clientDao.clients()
    }

    override suspend fun clientById(clientId: String): ClientRepositoryResponse = Either.catch {
        clientDao.clientById(clientId)
    }.mapLeft {
        ClientRepositoryUnexpectedError(
            description = "Error getting client $clientId",
            error = it,
        )
    }.leftIfNull {
        ClientNotFound(
            description = "Client $clientId not found",
        )
    }

    override suspend fun updateLocalClient(clientModel: ClientModel) {
        val cacheClient = clientModel.toCacheCreateClientEntity()

        cacheCreateClientDao.update(cacheClient)
    }

    override suspend fun uploadClient(
        clientModel: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): ClientModel {
        val currentStore = firebaseManager.currentStore?.uid ?: ""
        var currentUser = ""
        authenticationRepository
            .currentUser()
            .filterNotNull()
            .map { it.id }
            .take(1)
            .collect {
                currentUser = it
            }

        val fsClient = clientModel
            .toFSClient(currentStore)
            .copy(
                createdBy = currentUser,
                createAllowedBy = currentUser,
                updateAllowedBy = currentUser,
                updatedBy = currentUser,
            )

        val fsClientLegal = FSClientLegal(
            hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
            methodPromotionalMessages = ClientLegalMethod.SalesAppCreateAccount.toName(),

            createdBy = currentUser,
            createAllowedBy = currentUser,
            updateAllowedBy = currentUser,
            updatedBy = currentUser,
        )

        val clientResponse = clientDao.clientByDocument(clientModel.document)
        return clientResponse.fold(
            {
                Timber.i("Existing client not found")

                clientDao.addClient(
                    clientId = clientModel.id,
                    client = fsClient,
                )
                clientLegalDao.addClientLegalFor(
                    clientId = clientModel.id,
                    clientLegal = fsClientLegal,
                )

                clientModel.copy()
            },
            {
                clientDao.updateClient(
                    clientId = it.first,
                    client = fsClient,
                )

                clientLegalDao.updateClientLegalFor(
                    clientId = it.first,
                    clientLegal = fsClientLegal,
                )

                clientModel.copy(id = it.first)
            }
        )
    }

    override suspend fun clearCreateClientCache(clientId: String) {
        val client = cacheCreateClientDao.getById(clientId)

        if (client != null) {
            cacheCreateClientDao.deleteById(clientId)
        }
    }

    override suspend fun pictureForClient(clientId: String): Uri {
        val storageRef = firebaseManager.storage?.reference
            ?: FirebaseStorage.getInstance().reference

        val storagePicturePath = salesApplication
            .stringResource(R.string.storage_client_picture)
            .format(clientId)

        val picture = try {
            storageRef.child(storagePicturePath)
                .downloadUrl
                .await()
        } catch (e: Exception) {
            Timber.e("Failed to download client picture for $clientId", e)
            Uri.EMPTY
        }

        if (picture == null) {
            Timber.e("Client picture with id $clientId does not exist")
            return Uri.EMPTY
        }

        return picture
    }

    override suspend fun paginateData(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): ClientPaginationResponse = either {
        val localPaginator = if (paginator == null) {
            clientDao.simpleCollectionPaginator(query, config)
                .mapLeft { CreatePaginatorError(it.description, it.error) }
                .bind()
        } else {
            paginator
        }
        paginator = localPaginator

        ensureNotNull(localPaginator) {
            Unexpected("Failed to initialize paginator", null)
        }

        val clients = localPaginator.page()
            .mapLeft { FetchPageError(it.description, it.error) }
            .bind()

        clients.map { it.second.toDocument(it.first) }
    }

    override suspend fun getById(id: String): Either<ReadError, ClientDocument> {
        return clientById(id).mapLeft {
            FetchDataError(it.description, it.error)
        }
    }
}