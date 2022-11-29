package com.peyess.salesapp.data.dao.purchase.discount

import com.peyess.salesapp.data.model.sale.purchase.discount.group.FSDiscountGroup

interface DiscountGroupDao {
    suspend fun getById(id: String): FSDiscountGroup
}