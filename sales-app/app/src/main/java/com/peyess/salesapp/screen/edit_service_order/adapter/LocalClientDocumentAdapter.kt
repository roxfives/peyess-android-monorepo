package com.peyess.salesapp.screen.edit_service_order.adapter

import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.feature.service_order.model.Client

fun EditClientPickedDocument.toClient(): Client {
    return Client(
        id = id,
        soId = soId,
        clientRole = clientRole,
        nameDisplay = nameDisplay,
        name = name,
        sex = sex,
        email = email,
        document = document,
        shortAddress = shortAddress,
    )
}
