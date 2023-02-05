package com.peyess.salesapp.data.repository.local_client

import androidx.paging.PagingSource
import androidx.sqlite.db.SimpleSQLiteQuery
import arrow.core.Either
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.data.model.local_client.LocalClientStatusDocument
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryFetchError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryFetchStatusError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryInsertError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryStatusInsertError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryPagingError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryUpdateError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryStatusUpdateError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryStatusWriteError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryWriteError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import kotlinx.coroutines.flow.Flow

typealias LocalClientStatusCreateResponse = Either<LocalClientRepositoryStatusInsertError, Unit>
typealias LocalClientStatusUpdateResponse = Either<LocalClientRepositoryStatusUpdateError, Unit>
typealias LocalClientStatusReadResponse =
        Either<LocalClientRepositoryFetchStatusError, LocalClientStatusDocument>
typealias LocalClientStatusStreamResponse =
        Flow<Either<LocalClientRepositoryFetchStatusError, LocalClientStatusDocument>>

typealias LocalClientCreateResponse = Either<LocalClientRepositoryWriteError, Unit>
typealias LocalClientUpdateResponse = Either<LocalClientRepositoryWriteError, Unit>
typealias LocalClientReadSingleResponse =
        Either<LocalClientRepositoryFetchError, LocalClientDocument>
typealias LocalClientStreamSingleResponse =
        Flow<Either<LocalClientRepositoryFetchError, LocalClientDocument>>
typealias LocalClientPaginateResponse =
        Either<LocalClientRepositoryPagingError, () -> PagingSource<Int, LocalClientDocument>>

typealias LocalClientTotalResponse = Either<LocalClientRepositoryFetchError, Int>

interface LocalClientRepository {
    suspend fun totalClients(): LocalClientTotalResponse

    suspend fun updateClientStatus(
        clientStatus: LocalClientStatusDocument,
    ): LocalClientStatusUpdateResponse
    suspend fun clientStatus(): LocalClientStatusReadResponse
    fun streamClientStatus(): LocalClientStatusStreamResponse

    suspend fun insertClient(clientStatus: LocalClientDocument): LocalClientCreateResponse
    suspend fun updateClient(clientStatus: LocalClientDocument): LocalClientUpdateResponse
    suspend fun clientById(clientId: String): LocalClientReadSingleResponse
    suspend fun latestClientUpdated(): LocalClientReadSingleResponse
    fun streamClientById(clientId: String): LocalClientStreamSingleResponse
    fun paginateClients(query: PeyessQuery): LocalClientPaginateResponse
}
