package com.peyess.salesapp.repository.clients

import com.peyess.salesapp.dao.client.firestore.ClientDocument
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun clients(): Flow<List<ClientDocument>>

    fun clientById(clientId: String): Flow<ClientDocument?>

    fun addClient(client: ClientDocument): Flow<Boolean>
}