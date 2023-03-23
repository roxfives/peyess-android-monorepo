package com.peyess.salesapp.dao.sale.active_so

data class LocalServiceOrderDocument(
    val id: String = "",
    val hasPrescription: Boolean = true,
    val saleId: String = "",
    val clientName: String = "",
)

