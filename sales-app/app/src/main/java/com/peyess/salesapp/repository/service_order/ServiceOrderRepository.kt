package com.peyess.salesapp.repository.service_order

import androidx.paging.PagingSource
import arrow.core.Either
import com.google.firebase.firestore.QuerySnapshot
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryPaginationError
import kotlinx.coroutines.flow.Flow

typealias ServiceOrderPagingSourceFactory = () -> PagingSource<QuerySnapshot, ServiceOrderDocument>
typealias ServiceOrderPaginationResponse =
        Either<ServiceOrderRepositoryPaginationError, ServiceOrderPagingSourceFactory>

interface ServiceOrderRepository {
    fun paginateServiceOrders(peyessQuery: PeyessQuery): ServiceOrderPaginationResponse

    fun serviceOrders(): Flow<List<ServiceOrderDocument>>

    suspend fun add(serviceOrderDocument: ServiceOrderDocument)
}