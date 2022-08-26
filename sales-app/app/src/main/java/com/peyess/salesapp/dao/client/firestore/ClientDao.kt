package com.peyess.salesapp.dao.client.firestore

import kotlinx.coroutines.flow.Flow

interface ClientDao {
    fun clients(): Flow<List<Client>>

    fun addClient(client: Client): Flow<Boolean>
}