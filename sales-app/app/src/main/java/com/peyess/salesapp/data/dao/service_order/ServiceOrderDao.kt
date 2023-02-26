package com.peyess.salesapp.data.dao.service_order

import arrow.core.Either
import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.dao.service_order.errors.ServiceOrderDaoFetchError
import com.peyess.salesapp.data.dao.service_order.errors.ServiceOrderDaoUpdateError
import com.peyess.salesapp.data.dao.service_order.utils.ServiceOrderPagingSource
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrderUpdate
import kotlinx.coroutines.flow.Flow

typealias ServiceOrderFetchResponse = Either<ServiceOrderDaoFetchError, FSServiceOrder>

typealias UpdateServiceOrderResponse = Either<ServiceOrderDaoUpdateError, Unit>

interface ServiceOrderDao {
    fun paginateServiceOrder(query: Query): ServiceOrderPagingSource

    fun serviceOrders(): Flow<List<FSServiceOrder>>

    suspend fun add(serviceOrder: FSServiceOrder)

    suspend fun updateServiceOrder(
        serviceOrderId: String,
        serviceOrderUpdate: FSServiceOrderUpdate,
    ): UpdateServiceOrderResponse

    suspend fun serviceOrderById(serviceOrderId: String): ServiceOrderFetchResponse
}
