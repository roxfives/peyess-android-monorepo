package com.peyess.salesapp.dao.sale.active_sale

data class LocalSaleDocument(
    val id: String = "",
    val collaboratorUid: String = "",
    val active: Boolean = false,
    val isUploading: Boolean = false,
)

