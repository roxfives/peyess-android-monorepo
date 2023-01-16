package com.peyess.salesapp.data.adapter.local_sale.client_picked

import com.peyess.salesapp.data.model.client.Sex
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedDocument
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity


fun ClientPickedEntity.toClientPickedDocument(): ClientPickedDocument {
    return ClientPickedDocument(
        id = id,
        soId = soId,
        clientRole = clientRole,
        nameDisplay = nameDisplay,
        name = name,
        sex = Sex.fromName(sex),
        email = email,
        document = document,
        picture = picture,
        shortAddress = shortAddress,
    )
}