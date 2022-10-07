package com.peyess.salesapp.dao.service_order

import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import kotlinx.coroutines.flow.Flow

interface ServiceOrderDao {
    fun serviceOrders(): Flow<List<FSServiceOrder>>

    suspend fun add(serviceOrder: FSServiceOrder)
}