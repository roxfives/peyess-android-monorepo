package com.peyess.salesapp.data.dao.purchase

import arrow.core.Either
import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.dao.purchase.error.UpdatePurchaseDaoError
import com.peyess.salesapp.data.dao.purchase.utils.PurchasePagingSource
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate

typealias UpdatePurchaseResponse = Either<UpdatePurchaseDaoError, Unit>

interface PurchaseDao {
    suspend fun add(document: FSPurchase)

    suspend fun getById(id: String): FSPurchase?

    fun paginatePurchases(query: Query): PurchasePagingSource

    suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: FSPurchaseUpdate,
    ): UpdatePurchaseResponse
}