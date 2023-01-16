package com.peyess.salesapp.dao.client.firestore

import kotlinx.coroutines.flow.Flow

interface ClientDao {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientDocument?

    suspend fun addClient(clientId: String, client: FSClient)
}