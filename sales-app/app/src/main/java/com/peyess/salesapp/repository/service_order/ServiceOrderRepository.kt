package com.peyess.salesapp.repository.service_order

import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import kotlinx.coroutines.flow.Flow

interface ServiceOrderRepository {
    fun serviceOrders(): Flow<List<ServiceOrderDocument>>

    suspend fun add(serviceOrderDocument: ServiceOrderDocument)
}