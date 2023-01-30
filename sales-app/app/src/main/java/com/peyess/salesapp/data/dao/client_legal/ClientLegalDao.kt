package com.peyess.salesapp.data.dao.client_legal

import com.peyess.salesapp.data.model.client_legal.FSClientLegal

interface ClientLegalDao {
    suspend fun addClientLegalFor(clientId: String, clientLegal: FSClientLegal)
}