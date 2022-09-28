package com.peyess.salesapp.data.repository.client

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.data.adapter.client.toCacheCreateClientEntity
import com.peyess.salesapp.data.adapter.client.toClientModel
import com.peyess.salesapp.data.adapter.client.toFSClient
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.utils.file.deleteFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val cacheCreateClientDao: CacheCreateClientDao,
    private val clientDao: ClientDao,
    private val authenticationRepository: AuthenticationRepository,
): ClientRepository {
    private val Context.dataStoreLatestClient: DataStore<Preferences>
            by preferencesDataStore(latestClientFilename)

    private val storageRef = FirebaseStorage.getInstance().reference

    override fun clients(): Flow<List<ClientDocument>> {
        return clientDao.clients()
    }

    override fun clientById(clientId: String): Flow<ClientDocument?> {
        return clientDao.clientById(clientId)
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

    override suspend fun uploadClient(clientModel: ClientModel) {
        val updatedUri = if (clientModel.picture != Uri.EMPTY) {
            uploadClientProfilePicture(clientModel)
        } else {
            Uri.EMPTY
        }

        val currentStore = firebaseManager.currentStore?.uid ?: ""
        var currentUser = ""
        authenticationRepository
            .currentUser()
            .retryWhen { _, attempt -> attempt < 5}
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
                createdAllowedBy = currentUser,
                updatedAllowedBy = currentUser,
                updatedBy = currentUser,
            )

        clientDao.addClient(
            clientId = clientModel.id,
            client = fsClient,
        )
    }

    override suspend fun clearCreateClientCache(clientId: String) {
        val client = cacheCreateClientDao.find(clientId)

        if (client != null) {
            deleteFile(client.picture)
            cacheCreateClientDao.deleteById(clientId)
        }
    }

    companion object {
        const val latestClientFilename =
            "com.peyess.salesapp.data.repository.client.cache_create_client_datasource"

        val latestClientKey = stringPreferencesKey("latest_client_key")
    }
}