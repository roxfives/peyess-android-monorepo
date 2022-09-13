package com.peyess.salesapp.repository.service_order

import com.peyess.salesapp.dao.service_order.ServiceOrderDocument
import kotlinx.coroutines.flow.Flow

interface ServiceOrderRepository {
    fun serviceOrders(): Flow<List<ServiceOrderDocument>>
}