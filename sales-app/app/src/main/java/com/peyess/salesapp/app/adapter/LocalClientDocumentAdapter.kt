package com.peyess.salesapp.app.adapter

import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientEntity

fun LocalClientDocument.toClient(): Client {
    return Client(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        birthday = birthday,
        document = document,
        sex = sex,
        shortAddress = shortAddress,
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

fun LocalClientEntity.toClient(): Client {
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
