package com.peyess.salesapp.data.repository.payment

import arrow.core.Either
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.purchase.toFSPurchase
import com.peyess.salesapp.data.adapter.purchase.toPurchaseDocument
import com.peyess.salesapp.data.adapter.service_order.toServiceOrderDocument
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.purchase.PurchaseDao
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.repository.payment.error.PurchaseRepositoryPaginationError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryPaginationError
import com.peyess.salesapp.utils.room.MappingPagingSource
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val purchaseDao: PurchaseDao,
): PurchaseRepository {
    override suspend fun add(purchase: PurchaseDocument) {
        val fsPositioning = purchase.toFSPurchase()

        purchaseDao.add(fsPositioning)
    }

    override suspend fun getById(id: String): PurchaseDocument? {
        return purchaseDao.getById(id)?.toPurchaseDocument()
    }

    override fun paginatePurchases(
        peyessQuery: PeyessQuery,
    ): PurchasePaginationResponse = Either.catch {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid ?: ""
        val collectionPath = salesApplication
            .stringResource(id = R.string.fs_col_purchase)
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
                originalSource = purchaseDao.paginatePurchases(query),
                mapper = { it.second.toPurchaseDocument() }
            )
        }

        pagingSourceFactory
    }.mapLeft {
        PurchaseRepositoryPaginationError.Unexpected(
            description = it.message ?: "Unknown error",
            throwable = it,
        )
    }
}
