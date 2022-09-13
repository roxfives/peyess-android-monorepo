package com.peyess.salesapp.repository.service_order

import com.peyess.salesapp.dao.service_order.ServiceOrderDao
import com.peyess.salesapp.dao.service_order.ServiceOrderDocument
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ServiceOrderRepositoryImpl @Inject constructor(
    private val serviceOrderDao: ServiceOrderDao,
): ServiceOrderRepository {
    override fun serviceOrders(): Flow<List<ServiceOrderDocument>> {
        return serviceOrderDao.serviceOrders()
    }
}