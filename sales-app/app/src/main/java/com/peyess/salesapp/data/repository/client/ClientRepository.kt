package com.peyess.salesapp.data.repository.client

import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientModel
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun clients(): Flow<List<ClientDocument>>

    fun clientById(clientId: String): Flow<ClientDocument?>

    fun latestLocalClientCreated(): Flow<ClientModel?>
    suspend fun updateLocalClient(clientModel: ClientModel)
    suspend fun createNewLocalClient()
    suspend fun cancelLocalClientCreation()
}
