package com.peyess.salesapp.data.model.local_sale.client_picked

import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole

data class ClientPickedDocument(
    val id: String = "",
    val soId: String = "",
    val clientRole: ClientRole = ClientRole.User,
    val nameDisplay: String = "",
    val name: String = "",
    val sex: Sex = Sex.Unknown,
    val email: String = "",
    val document: String = "",
    val shortAddress: String = "",
)
