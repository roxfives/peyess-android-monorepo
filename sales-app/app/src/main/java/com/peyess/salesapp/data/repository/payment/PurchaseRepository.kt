package com.peyess.salesapp.data.repository.payment

import androidx.paging.PagingSource
import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.repository.payment.error.PurchaseRepositoryPaginationError
import com.peyess.salesapp.data.utils.query.PeyessQuery

typealias PurchasePagingSourceFactory = () -> PagingSource<DocumentSnapshot, PurchaseDocument>
typealias PurchasePaginationResponse =
        Either<PurchaseRepositoryPaginationError, PurchasePagingSourceFactory>

interface PurchaseRepository {
    suspend fun add(purchase: PurchaseDocument)

    suspend fun getById(id: String): PurchaseDocument?

    fun paginatePurchases(peyessQuery: PeyessQuery): PurchasePaginationResponse
}
