package com.peyess.salesapp.repository.clients

import com.peyess.salesapp.dao.client.firestore.Client
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun clients(): Flow<List<Client>>

    fun addClient(client: Client): Flow<Boolean>
}