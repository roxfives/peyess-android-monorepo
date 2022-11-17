package com.peyess.salesapp.data.repository.products_table_state

import com.peyess.salesapp.data.adapter.products_table_state.toProductTableState
import com.peyess.salesapp.data.adapter.products_table_state.toProductsTableStatusEntity
import com.peyess.salesapp.data.dao.products_table_state.ProductsTableStateDao
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class ProductsTableStateRepositoryImpl @Inject constructor(
    private val productsTableStateDao: ProductsTableStateDao
): ProductsTableStateRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val latestTableFlow by lazy {
        productsTableStateDao
            .observeProductTableState(defaultId)
            .filterNotNull()
            .map { it.toProductTableState() }
            .shareIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )
    }

    private val defaultValue = ProductsTableStatus()

    private val defaultId = 0

    override suspend fun getCurrentState(): ProductsTableStatus {
        val tableEntity = productsTableStateDao.getProductTableState(defaultId)

        val all = productsTableStateDao.getAll()
        Timber.i("Got all (${all.size}): $all")

        return tableEntity?.toProductTableState() ?: defaultValue.copy()
    }

    override fun observeState(): Flow<ProductsTableStatus> {
        return latestTableFlow
    }

    override fun update(productsTableStatus: ProductsTableStatus) {
        val tableAsEntity = productsTableStatus.toProductsTableStatusEntity(defaultId)

        productsTableStateDao.update(tableAsEntity)
    }
}