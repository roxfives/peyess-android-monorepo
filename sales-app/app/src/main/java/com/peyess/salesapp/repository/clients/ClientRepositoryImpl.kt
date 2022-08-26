package com.peyess.salesapp.repository.clients

import com.peyess.salesapp.dao.client.firestore.Client
import com.peyess.salesapp.dao.client.firestore.ClientDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val clientDao: ClientDao,
): ClientRepository {
    override fun clients(): Flow<List<Client>> {
        return clientDao.clients()
    }

    override fun addClient(client: Client): Flow<Boolean> {
        return clientDao.addClient(client)
    }
}