package com.peyess.salesapp.data.dao.address_lookup

interface AddressLookupDao {
    suspend fun findByZipCode(code: String): ViaCEPResponse
}