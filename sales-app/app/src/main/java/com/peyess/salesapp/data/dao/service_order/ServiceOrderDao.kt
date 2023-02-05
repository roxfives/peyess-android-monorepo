package com.peyess.salesapp.data.dao.service_order

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.dao.service_order.utils.ServiceOrderPagingSource
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import kotlinx.coroutines.flow.Flow


interface ServiceOrderDao {
    fun paginateServiceOrder(query: Query): ServiceOrderPagingSource

    fun serviceOrders(): Flow<List<FSServiceOrder>>

    suspend fun add(serviceOrder: FSServiceOrder)
}