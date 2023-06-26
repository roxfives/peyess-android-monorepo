package com.peyess.salesapp.data.repository.payment

import arrow.core.Either
import arrow.core.continuations.either
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.purchase.toFSPurchase
import com.peyess.salesapp.data.adapter.purchase.toFSPurchaseUpdate
import com.peyess.salesapp.data.adapter.purchase.toMappingUpdate
import com.peyess.salesapp.data.adapter.purchase.toPurchaseDocument
import com.peyess.salesapp.data.dao.purchase.PurchaseDao
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseUpdateDocument
import com.peyess.salesapp.data.repository.payment.error.PurchaseRepositoryPaginationError
import com.peyess.salesapp.data.repository.payment.error.UpdatePurchaseRepositoryError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState
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

    override suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: PurchaseUpdateDocument
    ): UpdatePurchaseResponse = either {
        purchaseDao.updatePurchase(purchaseId, purchaseUpdate.toFSPurchaseUpdate())
            .mapLeft {
                UpdatePurchaseRepositoryError.Unexpected(
                    description = "Purchase with id $purchaseId not found",
                    throwable = null,
                )
            }.bind()
    }

    override suspend fun updatePurchaseStatus(
        purchaseId: String,
        status: PurchaseState,
        updatedBy: String
    ): UpdatePurchaseStateResponse = either {
        purchaseDao.updatePurchaseState(purchaseId, status, updatedBy)
            .map { purchaseId }
            .mapLeft {
                UpdatePurchaseRepositoryError.Unexpected(
                    description = "Purchase with id $purchaseId not found",
                    throwable = null,
                )
            }.bind()
    }

    override suspend fun updatePurchaseSyncStatus(
        purchaseId: String,
        status: PurchaseSyncState,
        updatedBy: String
    ): UpdatePurchaseStateResponse = either {
        purchaseDao.updatePurchaseSyncState(purchaseId, status, updatedBy)
            .map { purchaseId }
            .mapLeft {
                UpdatePurchaseRepositoryError.Unexpected(
                    description = "Purchase with id $purchaseId not found",
                    throwable = null,
                )
            }.bind()
    }
}
