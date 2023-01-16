package com.peyess.salesapp.data.repository.local_sale.client_picked

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedDocument
import com.peyess.salesapp.data.repository.local_sale.client_picked.error.ClientPickedReadError
import com.peyess.salesapp.typing.sale.ClientRole

typealias ClientPickedResponse = Either<ClientPickedReadError, ClientPickedDocument>

interface ClientPickedRepository {
    suspend fun getClientForServiceOrder(role: ClientRole, soId: String): ClientPickedResponse
}