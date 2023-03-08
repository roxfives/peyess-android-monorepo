package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSClient.toClientDocument(id: String): ClientDocument {
    return ClientDocument(
        id = id,
        name = name,
        nameDisplay = nameDisplay,
        birthday = birthday.toZonedDateTime(),
        document = document,
        sex = Sex.fromName(sex),
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
        storesIds = storesIds,
        doc_version = doc_version,
        is_editable = is_editable,
        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}

fun FSClient.toMap(): Map<String, Any> {
    return mapOf(
        "name" to name,
        "nameDisplay" to nameDisplay,
        "birthday" to birthday,
        "document" to document,
        "sex" to sex,
        "zipCode" to zipCode,
        "street" to street,
        "houseNumber" to houseNumber,
        "complement" to complement,
        "neighborhood" to neighborhood,
        "city" to city,
        "state" to state,
        "email" to email,
        "phone" to phone,
        "cellphone" to cellphone,
        "whatsapp" to whatsapp,
        "storesIds" to storesIds,
        "doc_version" to doc_version,
        "is_editable" to is_editable,
        "created" to created,
        "createdBy" to createdBy,
        "createAllowedBy" to createAllowedBy,
        "updated" to updated,
        "updatedBy" to updatedBy,
        "updateAllowedBy" to updateAllowedBy,
    )
}