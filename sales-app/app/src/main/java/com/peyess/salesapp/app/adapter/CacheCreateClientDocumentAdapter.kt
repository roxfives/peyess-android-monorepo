package com.peyess.salesapp.app.adapter

import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument

fun CacheCreateClientDocument.toClient(): Client {
    return Client(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        birthday = birthday,
        document = document,
        sex = sex,
        shortAddress = "$street, $houseNumber",
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
    )
}