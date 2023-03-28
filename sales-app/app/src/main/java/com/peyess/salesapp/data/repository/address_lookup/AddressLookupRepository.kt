package com.peyess.salesapp.data.repository.address_lookup

import com.peyess.salesapp.data.model.address_lookup.AddressModel

interface AddressLookupRepository {
    suspend fun findAddressByZipCode(zipCode: String): AddressModel
}