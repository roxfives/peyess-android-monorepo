package com.peyess.salesapp.data.repository.client

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.leftIfNull
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.client.ClientDao
import com.peyess.salesapp.data.adapter.client.toCacheCreateClientEntity
import com.peyess.salesapp.data.adapter.client.toClientModel
import com.peyess.salesapp.data.adapter.client.toFSClient
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.dao.client_legal.ClientLegalDao
import com.peyess.salesapp.data.internal.firestore.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.model.client.toDocument
import com.peyess.salesapp.data.model.client_legal.ClientLegalMethod
import com.peyess.salesapp.data.model.client_legal.FSClientLegal
import com.peyess.salesapp.data.repository.client.error.ClientNotFound
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryUnexpectedError
import com.peyess.salesapp.data.repository.internal.firestore.errors.CreatePaginatorError
import com.peyess.salesapp.data.repository.internal.firestore.errors.FetchPageError
import com.peyess.salesapp.data.repository.internal.firestore.errors.ReadError
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import com.peyess.salesapp.data.repository.internal.firestore.errors.Unexpected
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.utils.file.deleteFile
import com.peyess.salesapp.workmanager.clients.error.ClientWorkerUnexpectedError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
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
    private val Context.dataStoreLatestClient: DataStore<Preferences>
            by preferencesDataStore(latestClientFilename)

    private val storageRef = FirebaseStorage.getInstance().reference

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun latestLocalClientCreated(): Flow<ClientModel?> {
        return salesApplication
            .dataStoreLatestClient
            .data
            .flatMapLatest { prefs ->
                Timber.i("Getting latest client by id ${prefs[latestClientKey]}")

                cacheCreateClientDao.getById(prefs[latestClientKey] ?: "")
            }
            .map { it?.toClientModel() }
    }

    override suspend fun createNewLocalClient() {
        val id = firebaseManager.uniqueId()
        val client = CacheCreateClientEntity(id = id)

        Timber.i("Creating client with id $id")

        salesApplication.dataStoreLatestClient.edit { prefs ->
            Timber.i("Setting latest id to $id")

            prefs[latestClientKey] = id
        }

        Timber.i("Adding client to database $id")
        cacheCreateClientDao.add(client)
    }

    override suspend fun updateLocalClient(clientModel: ClientModel) {
        val cacheClient = clientModel.toCacheCreateClientEntity()

        cacheCreateClientDao.update(cacheClient)
    }

    override suspend fun cancelLocalClientCreation() {
        salesApplication
            .dataStoreLatestClient
            .data
            .map { prefs ->
                val id = prefs[latestClientKey] ?: ""

                cacheCreateClientDao.deleteById(id)
            }
            .take(1)
            .flowOn(Dispatchers.IO)
            .collect()
    }

    private suspend fun uploadClientProfilePicture(clientModel: ClientModel): Uri {
        val clientId = clientModel.id
        val path = salesApplication
            .stringResource(R.string.storage_client_profile_pic)
            .format(clientId)

        val profilePicRef = storageRef.child(path)
        var downloadUrl: Uri = Uri.EMPTY
        try {
            profilePicRef.putFile(clientModel.picture).await()
            downloadUrl = profilePicRef.downloadUrl.await()
        } catch (error: Throwable) {
            Timber.e("Error while uploading client profile picture", error)
        }

        return downloadUrl
    }

    override suspend fun uploadClient(
        clientModel: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ) {
        val updatedUri = if (clientModel.picture != Uri.EMPTY) {
            uploadClientProfilePicture(clientModel)
        } else {
            Uri.EMPTY
        }

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
                picture = updatedUri.toString(),
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

        clientDao.addClient(
            clientId = clientModel.id,
            client = fsClient,
        )
        clientLegalDao.addClientLegalFor(
            clientId = clientModel.id,
            clientLegal = fsClientLegal,
        )
    }

    override suspend fun clearCreateClientCache(clientId: String) {
        val client = cacheCreateClientDao.find(clientId)

        if (client != null) {
            deleteFile(client.picture)
            cacheCreateClientDao.deleteById(clientId)
        }
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
        TODO("Not yet implemented")
    }

    companion object {
        const val latestClientFilename =
            "com.peyess.salesapp.data.repository.client.cache_create_client_datasource"

        val latestClientKey = stringPreferencesKey("latest_client_key")
    }
}