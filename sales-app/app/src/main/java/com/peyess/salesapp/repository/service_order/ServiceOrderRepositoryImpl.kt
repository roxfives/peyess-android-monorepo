package com.peyess.salesapp.repository.service_order

import com.peyess.salesapp.data.adapter.service_order.toFSServiceOrder
import com.peyess.salesapp.data.adapter.service_order.toServiceOrderDocument
import com.peyess.salesapp.data.dao.service_order.ServiceOrderDao
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServiceOrderRepositoryImpl @Inject constructor(
    private val serviceOrderDao: ServiceOrderDao,
): ServiceOrderRepository {
    override fun serviceOrders(): Flow<List<ServiceOrderDocument>> {
        return serviceOrderDao
            .serviceOrders()
            .map { soList ->
                soList.map { it.toServiceOrderDocument() }
            }
    }

    override suspend fun add(serviceOrderDocument: ServiceOrderDocument) {
        serviceOrderDao.add(serviceOrderDocument.toFSServiceOrder())
    }
}