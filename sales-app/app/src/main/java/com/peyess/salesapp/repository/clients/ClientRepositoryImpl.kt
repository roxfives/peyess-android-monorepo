package com.peyess.salesapp.repository.clients

import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.firestore.ClientDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
): ClientRepository {
    override fun clients(): Flow<List<ClientDocument>> {
        return clientDao.clients()
    }

    override fun clientById(clientId: String): Flow<ClientDocument?> {
        return clientDao.clientById(clientId)
    }

    override fun addClient(client: ClientDocument): Flow<Boolean> {
        return clientDao.addClient(client)
    }
}