package com.peyess.salesapp.data.repository.client

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.repository.client.error.ExistsClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.UpdateClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.UploadClientRepositoryError
import com.peyess.salesapp.data.repository.internal.firestore.ReadOnlyRepository
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import kotlinx.coroutines.flow.Flow

typealias ClientRepositoryResponse = Either<ClientRepositoryError, ClientDocument>
typealias ClientPaginationResponse = Either<RepositoryError, List<ClientDocument>>
typealias ExistsClientIdResponse = Either<ExistsClientRepositoryError, Boolean>
typealias ExistsClientDocumentResponse = Either<ExistsClientRepositoryError, String>
typealias UploadClientResponse = Either<UploadClientRepositoryError, Unit>
typealias UpdateClientResponse = Either<UpdateClientRepositoryError, Unit>

interface ClientRepository: ReadOnlyRepository<ClientDocument> {
    fun clients(): Flow<List<ClientDocument>>

    suspend fun clientById(clientId: String): ClientRepositoryResponse

    suspend fun uploadClient(
        clientModel: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): UploadClientResponse
    suspend fun updateClient(
        clientModel: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): UpdateClientResponse

    suspend fun clearCreateClientCache(clientId: String)

    suspend fun clientExistsById(clientId: String): ExistsClientIdResponse
    suspend fun clientExistsByDocument(clientDocument: String): ExistsClientDocumentResponse

    suspend fun pictureForClient(clientId: String): Uri
}
