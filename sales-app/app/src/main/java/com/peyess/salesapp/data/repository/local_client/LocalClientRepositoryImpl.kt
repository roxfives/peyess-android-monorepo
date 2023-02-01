package com.peyess.salesapp.data.repository.local_client

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.local_client.toLocalClientDocument
import com.peyess.salesapp.data.adapter.local_client.toLocalClientEntity
import com.peyess.salesapp.data.adapter.local_client.toLocalClientStatusDocument
import com.peyess.salesapp.data.adapter.local_client.toLocalClientStatusEntity
import com.peyess.salesapp.data.dao.local_client.LocalClientDao
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.data.model.local_client.LocalClientStatusDocument
import com.peyess.salesapp.data.repository.local_client.error.LocalClientNotFoundError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryCreatePagingSourceError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryInsertError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryReadStatusError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryStatusNotFoundError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryStatusUpdateError
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryUpdateError
import com.peyess.salesapp.data.repository.local_client.error.UnexpectedLocalClientRepositoryError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toSqlQuery
import com.peyess.salesapp.utils.room.MappingPagingSource
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val clientStatusId = 0L

class LocalClientRepositoryImpl @Inject constructor(
    private val localClientDao: LocalClientDao,
): LocalClientRepository {
    override suspend fun updateClientStatus(
        clientStatus: LocalClientStatusDocument,
    ): LocalClientStatusUpdateResponse = Either.catch {
        val entity = clientStatus
            .toLocalClientStatusEntity()
            .copy(id = clientStatusId)

        localClientDao.insertClientStatus(entity)
    }.mapLeft {
        LocalClientRepositoryStatusUpdateError(
            description = "Error updating client status: $it",
            error = it,
        )
    }

    override suspend fun clientStatus(): LocalClientStatusReadResponse = Either.catch {
        localClientDao.clientStatus(clientStatusId)?.toLocalClientStatusDocument()
    }.mapLeft {
        LocalClientRepositoryReadStatusError(
            description = "Error reading client status: $it",
            error = it,
        )
    }.leftIfNull {
        UnexpectedLocalClientRepositoryError(
            description = "Client status not found",
            error = null,
        )
    }

    override fun streamClientStatus(): LocalClientStatusStreamResponse {
        return localClientDao.streamClientStatus(clientStatusId)
            .map {
               if (it == null) {
                   LocalClientRepositoryStatusNotFoundError(
                          description = "Client status not found",
                          error = null,
                     ).left()
               } else {
                   it.toLocalClientStatusDocument().right()
               }
            }
    }

    override suspend fun insertClient(
        clientStatus: LocalClientDocument,
    ): LocalClientCreateResponse = Either.catch {
        val entity = clientStatus.toLocalClientEntity()

        localClientDao.insertClient(entity)
    }.mapLeft {
        LocalClientRepositoryInsertError(
            description = "Error inserting client: $it",
            error = it,
        )
    }

    override suspend fun updateClient(
        clientStatus: LocalClientDocument,
    ): LocalClientUpdateResponse = Either.catch {
        val entity = clientStatus.toLocalClientEntity()

        localClientDao.updateClient(entity)
    }.mapLeft {
        LocalClientRepositoryUpdateError(
            description = "Error updating client: $it",
            error = it,
        )
    }

    override suspend fun clientById(
        clientId: String,
    ): LocalClientReadSingleResponse = Either.catch {
        localClientDao.clientById(clientId)?.toLocalClientDocument()
    }.mapLeft {
        UnexpectedLocalClientRepositoryError(
            description = "Error reading client: $it",
            error = it,
        )
    }.leftIfNull {
        LocalClientNotFoundError(
            description = "Client not found",
            error = null,
        )
    }

    override suspend fun latestClientUpdated(): LocalClientReadSingleResponse = Either.catch {
        localClientDao.latestClientUpdated()?.toLocalClientDocument()
    }.mapLeft {
        UnexpectedLocalClientRepositoryError(
            description = "Error reading client: $it",
            error = it,
        )
    }.leftIfNull {
        LocalClientNotFoundError(
            description = "Client not found",
            error = null,
        )
    }

    override fun streamClientById(
        clientId: String,
    ): LocalClientStreamSingleResponse {
        return localClientDao.streamClientById(clientId)
            .map {
                if (it == null) {
                     LocalClientNotFoundError(
                              description = "Client not found",
                              error = null,
                        ).left()
                } else {
                     it.toLocalClientDocument().right()
                }
            }
    }

    override fun paginateClients(
        query: PeyessQuery,
    ): LocalClientPaginateResponse = Either.catch {
        val selectStatement = "SELECT * FROM ${LocalClientEntity.tableName}"
        val sqlQuery = query.toSqlQuery(selectStatement)

        val pagingSourceFactory =  {
            MappingPagingSource(originalSource = localClientDao.paginateClients(sqlQuery),
                buildNewSource = { localClientDao.paginateClients(sqlQuery) },
                mapper = { it.toLocalClientDocument() })
        }

        pagingSourceFactory
    }.mapLeft {
        LocalClientRepositoryCreatePagingSourceError(
            description = "Error paginating clients: $it",
            error = it,
        )
    }
}
