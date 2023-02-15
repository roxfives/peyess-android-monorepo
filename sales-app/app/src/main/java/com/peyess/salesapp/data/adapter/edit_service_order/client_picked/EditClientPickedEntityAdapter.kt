package com.peyess.salesapp.data.adapter.edit_service_order.client_picked

import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedEntity

fun EditClientPickedEntity.toEditClientPickedDocument(): EditClientPickedDocument {
    return EditClientPickedDocument(
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
