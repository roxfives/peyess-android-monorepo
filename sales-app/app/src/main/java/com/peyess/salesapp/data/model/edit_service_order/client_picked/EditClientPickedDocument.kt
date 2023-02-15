package com.peyess.salesapp.data.model.edit_service_order.client_picked

import com.peyess.salesapp.typing.sale.ClientRole

data class EditClientPickedDocument(
    val id: String = "",
    val soId: String = "",
    val clientRole: ClientRole = ClientRole.User,
    val nameDisplay: String = "",
    val name: String = "",
    val sex: String = "",
    val email: String = "",
    val document: String = "",
    val shortAddress: String = "",
)