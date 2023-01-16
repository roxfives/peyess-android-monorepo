package com.peyess.salesapp.data.repository.local_sale.client_picked

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.adapter.local_sale.client_picked.toClientPickedDocument
import com.peyess.salesapp.data.dao.local_sale.client_picked.ClientPickedDao
import com.peyess.salesapp.data.repository.local_sale.client_picked.error.ClientPickedNotFound
import com.peyess.salesapp.data.repository.local_sale.client_picked.error.Unexpected
import com.peyess.salesapp.typing.sale.ClientRole
import javax.inject.Inject

class ClientPickedRepositoryImpl @Inject constructor(
    private val clientPickedDao: ClientPickedDao,
): ClientPickedRepository {
    override suspend fun getClientForServiceOrder(
        role: ClientRole,
        soId: String,
    ): ClientPickedResponse = Either.catch {
        clientPickedDao.getClientForServiceOrder(role, soId)
            ?.toClientPickedDocument()
    }.mapLeft {
        Unexpected(
            description = "Failed to get client for service order $soId using role $role",
            error = it,
        )
    }.leftIfNull {
        ClientPickedNotFound(
            description = "Client for service order $soId using role $role not found",
        )
    }
}
