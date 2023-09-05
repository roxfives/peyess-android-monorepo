package com.peyess.salesapp.data.repository.payment

import androidx.paging.PagingSource
import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseUpdateDocument
import com.peyess.salesapp.data.repository.payment.error.PurchaseRepositoryPaginationError
import com.peyess.salesapp.data.repository.payment.error.UpdatePurchaseRepositoryError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState

typealias PurchasePagingSourceFactory = () -> PagingSource<DocumentSnapshot, PurchaseDocument>
typealias PurchasePaginationResponse =
        Either<PurchaseRepositoryPaginationError, PurchasePagingSourceFactory>

typealias UpdatePurchaseResponse = Either<UpdatePurchaseRepositoryError, Unit>
typealias UpdatePurchaseStateResponse = Either<UpdatePurchaseRepositoryError, String>

interface PurchaseRepository {
    suspend fun add(purchase: PurchaseDocument)

    suspend fun getById(id: String): PurchaseDocument?

    fun paginatePurchases(peyessQuery: PeyessQuery): PurchasePaginationResponse

    suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: PurchaseUpdateDocument,
    ): UpdatePurchaseResponse

    suspend fun updatePurchaseStatus(
        purchaseId: String,
        status: PurchaseState,
        updatedBy: String,
    ): UpdatePurchaseStateResponse

    suspend fun updatePurchaseStatusAndFinish(
        purchaseId: String,
        status: PurchaseState,
        updatedBy: String,
    ): UpdatePurchaseStateResponse

    suspend fun updatePurchaseSyncStatus(
        purchaseId: String,
        status: PurchaseSyncState,
        updatedBy: String,
    ): UpdatePurchaseStateResponse
}
