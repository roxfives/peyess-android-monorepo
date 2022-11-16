package com.peyess.salesapp.data.adapter.products_table_state

import com.peyess.salesapp.data.dao.products_table_state.ProductTableStateEntity
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus

fun ProductsTableStatus.toProductsTableStatusEntity(): ProductTableStateEntity {
    return ProductTableStateEntity (
        id = 0,
        hasUpdated = hasUpdated,
        hasUpdateFailed = hasUpdateFailed,
        isUpdating = isUpdating,
    )
}