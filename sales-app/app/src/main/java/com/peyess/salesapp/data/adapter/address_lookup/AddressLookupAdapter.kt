package com.peyess.salesapp.data.adapter.address_lookup

import com.peyess.salesapp.data.dao.address_lookup.ViaCEPResponse
import com.peyess.salesapp.data.model.address_lookup.AddressModel

fun ViaCEPResponse.toAddressModel(): AddressModel {
    return AddressModel(
        state = uf,
        city = localidade,
        zipCode = cep,
        neighborhood = bairro,
        street = logradouro,
        complement = complemento,
        publicPlace = "",
        ibge = ibge,
        siafi = siafi,
        gia = gia,
    )
}