package com.peyess.salesapp.data.model.products_table_state

data class ProductsTableStatus(
    val hasUpdated: Boolean = false,
    val hasUpdateFailed: Boolean = false,
    val isUpdating: Boolean = false,
)
