package com.peyess.salesapp.data.adapter.products_table_state

import com.peyess.salesapp.data.dao.products_table_state.ProductTableStateEntity
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus

fun ProductTableStateEntity.toProductTableState(): ProductsTableStatus {
    return ProductsTableStatus(
        hasUpdated = hasUpdated,
        hasUpdateFailed = hasUpdateFailed,
        isUpdating = isUpdating,
    )
}