package com.peyess.salesapp.data.dao.client

import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.FSClient
import kotlinx.coroutines.flow.Flow

interface ClientDao {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientDocument?

    suspend fun addClient(clientId: String, client: FSClient)
}