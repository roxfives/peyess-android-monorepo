package com.peyess.salesapp.data.model.client

import android.net.Uri
import com.peyess.salesapp.data.model.client.FSClient
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.utils.time.toZonedDateTime
import java.time.ZonedDateTime

data class ClientDocument(
    val id: String = "",

    val name: String = "",
    val nameDisplay: String = "",

    val birthday: ZonedDateTime = ZonedDateTime.now(),

    val document: String = "",
    val sex: Sex = Sex.Unknown,

    val zipCode: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val complement: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",

    val email: String = "",
    val phone: String = "",
    val cellphone: String = "",
    val whatsapp: String = "",

    val storesIds: List<String> = emptyList(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
) {
    val shortAddress = "$city, $state"
}

fun ClientDocument.toClientPickedEntity(
    soId: String,
    role: ClientRole,
): ClientPickedEntity {
    return ClientPickedEntity(
        id = id,
        soId = soId,
        clientRole = role,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex.toName(),
        email = email,
        document = document,
        shortAddress = shortAddress,
    )
}

fun FSClient.toDocument(id: String): ClientDocument {
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
