package com.peyess.salesapp.data.dao.purchase

import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument

interface PurchaseDao {
    suspend fun add(document: FSPurchase)

    suspend fun getById(id: String): FSPurchase?
}