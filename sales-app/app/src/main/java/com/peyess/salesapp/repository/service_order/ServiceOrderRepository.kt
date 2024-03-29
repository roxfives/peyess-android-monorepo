package com.peyess.salesapp.repository.service_order

import androidx.paging.PagingSource
import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderUpdateDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryFetchError
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryPaginationError
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryUpdateError
import kotlinx.coroutines.flow.Flow

typealias ServiceOrderPagingSourceFactory = () -> PagingSource<DocumentSnapshot, ServiceOrderDocument>
typealias ServiceOrderPaginationResponse =
        Either<ServiceOrderRepositoryPaginationError, ServiceOrderPagingSourceFactory>
typealias ServiceOrderFetchResponse = Either<ServiceOrderRepositoryFetchError, ServiceOrderDocument>

typealias ServiceOrderRepositoryUpdateResponse = Either<ServiceOrderRepositoryUpdateError, Unit>

interface ServiceOrderRepository {
    fun paginateServiceOrders(peyessQuery: PeyessQuery): ServiceOrderPaginationResponse

    fun serviceOrders(): Flow<List<ServiceOrderDocument>>

    suspend fun add(serviceOrderDocument: ServiceOrderDocument)

    suspend fun update(
        serviceOrderId: String,
        serviceOrderDocument: ServiceOrderUpdateDocument,
    ): ServiceOrderRepositoryUpdateResponse

    suspend fun serviceOrderById(serviceOrderId: String): ServiceOrderFetchResponse
}
