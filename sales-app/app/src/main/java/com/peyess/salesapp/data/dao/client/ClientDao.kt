package com.peyess.salesapp.data.dao.client

import arrow.core.Either
import com.peyess.salesapp.data.dao.client.error.ReadClientDaoError
import com.peyess.salesapp.data.dao.client.error.UpdateClientDaoError
import com.peyess.salesapp.data.dao.internal.firestore.ReadOnlyFirestoreDao
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.FSClient
import kotlinx.coroutines.flow.Flow

typealias ReadClientResponse = Either<ReadClientDaoError, Pair<String, FSClient>>
typealias UpdateClientResponse = Either<UpdateClientDaoError, Unit>

interface ClientDao: ReadOnlyFirestoreDao<FSClient> {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientDocument?
    suspend fun clientByDocument(document: String): ReadClientResponse

    suspend fun addClient(clientId: String, client: FSClient)
    suspend fun updateClient(clientId: String, client: FSClient): UpdateClientResponse
}