package com.peyess.salesapp.data.repository.client

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.UpdateClientError
import com.peyess.salesapp.data.repository.internal.firestore.ReadOnlyRepository
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import kotlinx.coroutines.flow.Flow

typealias ClientRepositoryResponse = Either<ClientRepositoryError, ClientDocument>
typealias ClientPaginationResponse = Either<RepositoryError, List<ClientDocument>>
typealias UploadClientResponse = Either<UpdateClientError, Boolean>

interface ClientRepository: ReadOnlyRepository<ClientDocument> {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientRepositoryResponse

    suspend fun updateLocalClient(clientModel: ClientModel)

    suspend fun uploadClient(
        clientModel: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): ClientModel
    suspend fun clearCreateClientCache(clientId: String)

    suspend fun pictureForClient(clientId: String): Uri
}
