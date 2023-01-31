package com.peyess.salesapp.data.adapter.client

import com.google.firebase.Timestamp
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.data.model.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.ClientModel

fun CacheCreateClientEntity.toClientModel(): ClientModel {
    return ClientModel(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
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
    )
}

fun ClientModel.toCacheCreateClientEntity(): CacheCreateClientEntity {
    return CacheCreateClientEntity(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
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
    )
}

fun ClientModel.toFSClient(storeId: String): FSClient {
    val instantBirthday = birthday.toInstant()
    val timestampBirthday = Timestamp(instantBirthday.epochSecond, instantBirthday.nano)

    return FSClient(
        name = name,
        nameDisplay = nameDisplay,
        birthday = timestampBirthday,
        document = document,
        sex = sex.toName(),

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

        storesIds = if (storeId.isBlank()) emptyList() else listOf(storeId),
    )
}

fun ClientModel.toClientDocument(): ClientDocument {
    return ClientDocument(
        id = id,

        name = name,
        nameDisplay = nameDisplay,
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
    )
}