package com.peyess.salesapp.feature.sale.pick_client.adapter

import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.feature.sale.pick_client.model.Client

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