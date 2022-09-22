package com.peyess.salesapp.data.dao.address_lookup

import com.peyess.salesapp.data.internal.ktor.ktorHttpClient
import io.ktor.client.request.get

class AddressLookupDaoImpl: AddressLookupDao {
    private val baseUrl = "https://viacep.com.br/ws"
    private val responseFormat = "json"

    private fun buildRequestUrl(code: String): String {
        return "$baseUrl/$code/$responseFormat"
    }

    override suspend fun findByZipCode(code: String): ViaCEPResponse =
        ktorHttpClient.get(buildRequestUrl(code))
}