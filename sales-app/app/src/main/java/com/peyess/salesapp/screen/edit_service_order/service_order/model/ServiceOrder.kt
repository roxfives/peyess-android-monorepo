package com.peyess.salesapp.screen.edit_service_order.service_order.model

import com.peyess.salesapp.typing.lens.LensTypeCategoryName

data class ServiceOrder(
    val id: String = "",
    val hasPrescription: Boolean = true,
    val saleId: String = "",
    val clientName: String = "",
)
