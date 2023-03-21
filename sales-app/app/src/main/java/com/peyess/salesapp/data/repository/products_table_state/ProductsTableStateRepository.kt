package com.peyess.salesapp.data.repository.products_table_state

import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import kotlinx.coroutines.flow.Flow

interface ProductsTableStateRepository {
    suspend fun getCurrentState(): ProductsTableStatus

    fun observeState(): Flow<ProductsTableStatus>

    fun update(productsTableStatus: ProductsTableStatus)

    suspend fun cancelUpdate()
}