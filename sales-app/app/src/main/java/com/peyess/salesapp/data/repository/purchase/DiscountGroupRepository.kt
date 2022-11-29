package com.peyess.salesapp.data.repository.purchase

import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument

interface DiscountGroupRepository {
    suspend fun getById(id: String): DiscountGroupDocument
}