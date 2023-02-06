package com.peyess.salesapp.repository.service_order

import arrow.core.Either
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.lenses.toStoreLensWithDetailsDocument
import com.peyess.salesapp.data.adapter.service_order.toFSServiceOrder
import com.peyess.salesapp.data.adapter.service_order.toServiceOrderDocument
import com.peyess.salesapp.data.dao.service_order.ServiceOrderDao
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryPaginationError
import com.peyess.salesapp.utils.room.MappingPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ServiceOrderRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val serviceOrderDao: ServiceOrderDao,
): ServiceOrderRepository {
    override fun paginateServiceOrders(
        peyessQuery: PeyessQuery,
    ): ServiceOrderPaginationResponse = Either.catch {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid ?: ""
        val collectionPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        if (firestore == null) {
            throw Throwable("Firestore instance is null")
        }

        if (storeId.isBlank()) {
            throw Throwable("Store id is blank")
        }

        val query = peyessQuery.toFirestoreCollectionQuery(
            path = collectionPath,
            firestore = firestore,
        )

        val pagingSourceFactory = {
            MappingPagingSource(
                originalSource = serviceOrderDao.paginateServiceOrder(query),
                mapper = { it.second.toServiceOrderDocument() }
            )
        }

        pagingSourceFactory
    }.mapLeft {
        ServiceOrderRepositoryPaginationError.Unexpected(
            description = it.message ?: "Unknown error",
            throwable = it,
        )
    }

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