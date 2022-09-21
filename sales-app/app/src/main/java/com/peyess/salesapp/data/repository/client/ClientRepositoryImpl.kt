package com.peyess.salesapp.data.repository.client

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.data.adapter.client.toCacheCreateClientEntity
import com.peyess.salesapp.data.adapter.client.toClientModel
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val cacheCreateClientDao: CacheCreateClientDao,
    private val clientDao: ClientDao,
): ClientRepository {
    private val Context.dataStoreLatestClient: DataStore<Preferences>
            by preferencesDataStore(latestClientFilename)

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
                cacheCreateClientDao
                    .getById(prefs[latestClientKey] ?: "")
            }
            .map { it?.toClientModel() }
    }

    override suspend fun createNewLocalClient() {
        val id = firebaseManager.uniqueId()
        val client = CacheCreateClientEntity(id = id)

        salesApplication.dataStoreLatestClient.edit { prefs ->
            prefs[latestClientKey] = id
        }
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

    companion object {
        const val latestClientFilename =
            "com.peyess.salesapp.data.repository.client.cache_create_client_datasource"

        val latestClientKey = stringPreferencesKey("latest_client_key")
    }
}