package com.peyess.salesapp.data.dao.client_legal

import arrow.core.Either
import com.peyess.salesapp.data.dao.client_legal.error.FetchClientLegalDaoError
import com.peyess.salesapp.data.dao.client_legal.error.UpdateClientLegalDaoError
import com.peyess.salesapp.data.model.client_legal.FSClientLegal

typealias FetchClientLegalResponse = Either<FetchClientLegalDaoError, FSClientLegal>
typealias UpdateClientLegalResponse = Either<UpdateClientLegalDaoError, Unit>

interface ClientLegalDao {
    suspend fun legalForClient(clientId: String): FetchClientLegalResponse

    suspend fun addClientLegalFor(clientId: String, clientLegal: FSClientLegal)

    suspend fun updateClientLegalFor(
        clientId: String,
        clientLegal: FSClientLegal,
    ): UpdateClientLegalResponse
}