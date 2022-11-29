package com.peyess.salesapp.data.repository.discount

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import kotlinx.coroutines.flow.Flow

interface OverallDiscountRepository {
    fun watchDiscountForSale(saleId: String): Flow<OverallDiscountDocument?>

    suspend fun getDiscountForSale(saleId: String): OverallDiscountDocument?

    suspend fun updateDiscountForSale(discount: OverallDiscountDocument)
}