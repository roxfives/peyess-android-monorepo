package com.peyess.salesapp.data.repository.address_lookup

import com.peyess.salesapp.data.adapter.address_lookup.toAddressModel
import com.peyess.salesapp.data.dao.address_lookup.AddressLookupDao
import com.peyess.salesapp.data.model.address_lookup.AddressModel
import javax.inject.Inject

class AddressLookupRepositoryImpl @Inject constructor(
    private val addressLookupDao: AddressLookupDao,
): AddressLookupRepository {
    override suspend fun findAddressByZipCode(zipCode: String): AddressModel {
        val address = addressLookupDao.findByZipCode(zipCode)

        if (address.erro) {
            throw Error("Failed to find address at $zipCode")
        }

        return address.toAddressModel()
    }
}