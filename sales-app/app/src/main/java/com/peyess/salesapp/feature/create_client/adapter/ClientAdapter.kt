package com.peyess.salesapp.feature.create_client.adapter

import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.feature.create_client.model.Client

fun Client.toCacheCreateClientDocument(): CacheCreateClientDocument {
    return CacheCreateClientDocument(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
        birthday = birthday,
        document = document,
        sex = sex,
        zipCode = zipCode,
        street = street,
        houseNumber = houseNumber,
        complement = complement,
        neighborhood = neighborhood,
        city = city,
        state = state,
        email = email,
        phone = phone,
        cellphone = cellphone,
        whatsapp = whatsapp,
        isCreating = isCreating,
    )
}
