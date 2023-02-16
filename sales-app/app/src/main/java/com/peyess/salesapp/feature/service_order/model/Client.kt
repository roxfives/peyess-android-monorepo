package com.peyess.salesapp.feature.service_order.model

import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole

data class Client(
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
