package com.peyess.salesapp.data.dao.purchase

import com.peyess.salesapp.data.model.sale.purchase.FSPurchase

interface PurchaseDao {
    suspend fun add(document: FSPurchase)
}