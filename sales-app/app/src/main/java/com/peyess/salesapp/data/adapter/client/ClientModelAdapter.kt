package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.dao.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientModel

fun CacheCreateClientEntity.toClientModel(): ClientModel {
    return ClientModel(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
        birthday = birthday.toZonedDateTime(),
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
    )
}

fun ClientModel.toCacheCreateClientEntity(): CacheCreateClientEntity {
    return CacheCreateClientEntity(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
        picture = picture,
        birthday = birthday.toOffsetDateTime(),
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
    )
}