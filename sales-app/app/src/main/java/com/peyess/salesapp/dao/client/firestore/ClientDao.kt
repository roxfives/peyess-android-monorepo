package com.peyess.salesapp.dao.client.firestore

import kotlinx.coroutines.flow.Flow

interface ClientDao {
    fun clients(): Flow<List<ClientDocument>>

    fun clientById(clientId: String): Flow<ClientDocument?>

    fun addClient(client: ClientDocument): Flow<Boolean>
}