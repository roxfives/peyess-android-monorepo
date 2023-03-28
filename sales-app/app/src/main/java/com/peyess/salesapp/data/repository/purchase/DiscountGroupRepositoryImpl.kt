package com.peyess.salesapp.data.repository.purchase

import com.peyess.salesapp.data.adapter.purchase.discount.group.toDiscountGroupDocument
import com.peyess.salesapp.data.dao.purchase.discount.DiscountGroupDao
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import javax.inject.Inject

class DiscountGroupRepositoryImpl @Inject constructor(
    private val discountGroupDao: DiscountGroupDao,
) : DiscountGroupRepository {
    override suspend fun getById(id: String): DiscountGroupDocument {
        return discountGroupDao.getById(id).toDiscountGroupDocument()
    }
}