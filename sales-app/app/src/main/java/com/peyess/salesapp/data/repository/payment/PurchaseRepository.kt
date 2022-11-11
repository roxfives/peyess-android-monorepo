package com.peyess.salesapp.data.repository.payment

import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument

interface PurchaseRepository {
    suspend fun add(purchase: PurchaseDocument)

    suspend fun getById(id: String): PurchaseDocument?
}