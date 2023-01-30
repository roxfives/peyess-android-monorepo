package com.peyess.salesapp.data.repository.client

import arrow.core.Either
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryError
import kotlinx.coroutines.flow.Flow

typealias ClientRepositoryResponse = Either<ClientRepositoryError, ClientDocument>

interface ClientRepository {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientRepositoryResponse

    fun latestLocalClientCreated(): Flow<ClientModel?>
    suspend fun updateLocalClient(clientModel: ClientModel)
    suspend fun createNewLocalClient()
    suspend fun cancelLocalClientCreation()

    suspend fun uploadClient(clientModel: ClientModel, hasAcceptedPromotionalMessages: Boolean)
    suspend fun clearCreateClientCache(clientId: String)
}
