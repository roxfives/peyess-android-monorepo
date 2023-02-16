package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.feature.service_order.model.Client
import com.peyess.salesapp.typing.client.Sex

fun ClientPickedEntity.toClient(): Client {
    return Client(
        id = id,
        soId = soId,
        clientRole = clientRole,
        nameDisplay = nameDisplay,
        name = name,
        sex = Sex.fromName(sex),
        email = email,
        document = document,
        shortAddress = shortAddress,
    )
}