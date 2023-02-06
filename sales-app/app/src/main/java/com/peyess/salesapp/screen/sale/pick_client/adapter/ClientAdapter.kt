package com.peyess.salesapp.screen.sale.pick_client.adapter

import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.screen.sale.pick_client.model.Client
import com.peyess.salesapp.typing.sale.ClientRole

fun Client.toClientPickedEntity(
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
