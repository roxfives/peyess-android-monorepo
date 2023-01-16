package com.peyess.salesapp.feature.sale.payment.adapter

import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.feature.sale.payment.model.Client

fun ClientDocument.toClient(): Client {
    return Client(
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
        storesIds = storesIds,
        doc_version = doc_version,
        is_editable = is_editable,
        created = created,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}