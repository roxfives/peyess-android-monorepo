package com.peyess.salesapp.data.dao.purchase

import arrow.core.Either
import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.dao.purchase.error.UpdatePurchaseDaoError
import com.peyess.salesapp.data.dao.purchase.utils.PurchasePagingSource
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState

typealias UpdatePurchaseResponse = Either<UpdatePurchaseDaoError, Unit>

interface PurchaseDao {
    suspend fun add(document: FSPurchase)

    suspend fun getById(id: String): FSPurchase?

    fun paginatePurchases(query: Query): PurchasePagingSource

    suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: FSPurchaseUpdate,
    ): UpdatePurchaseResponse

    suspend fun updatePurchaseState(
        purchaseId: String,
        state: PurchaseState,
        updatedBy: String,
    ): UpdatePurchaseResponse

    suspend fun updatePurchaseSyncState(
        purchaseId: String,
        state: PurchaseSyncState,
        updatedBy: String,
    ): UpdatePurchaseResponse
}