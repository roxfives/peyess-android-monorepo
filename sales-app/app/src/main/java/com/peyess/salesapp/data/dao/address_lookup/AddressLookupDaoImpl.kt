package com.peyess.salesapp.data.dao.address_lookup

import com.peyess.salesapp.data.internal.ktor.ktorHttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class AddressLookupDaoImpl: AddressLookupDao {
    private val baseUrl = "https://viacep.com.br/ws"
    private val responseFormat = "json"

    private val json = Json { ignoreUnknownKeys= true }

    private fun buildRequestUrl(code: String): String {
        return "$baseUrl/$code/$responseFormat"
    }

//    override suspend fun findByZipCode(code: String): ViaCEPResponse =
//        ktorHttpClient.get(buildRequestUrl(code))

    // https://github.com/Kotlin/kotlinx.serialization/issues/1105
    override suspend fun findByZipCode(code: String): ViaCEPResponse {
        return json.decodeFromString(
            ViaCEPResponse.serializer(),
            ktorHttpClient.get(buildRequestUrl(code)),
        )
    }
}