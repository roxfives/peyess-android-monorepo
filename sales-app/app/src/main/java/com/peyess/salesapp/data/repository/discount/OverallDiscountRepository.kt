package com.peyess.salesapp.data.repository.discount

import arrow.core.Either
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.repository.discount.error.OverallDiscountReadError
import kotlinx.coroutines.flow.Flow

typealias OverallDiscountRepositoryResponse =
        Either<OverallDiscountReadError, OverallDiscountDocument>

interface OverallDiscountRepository {
    fun watchDiscountForSale(saleId: String): Flow<OverallDiscountDocument?>

    suspend fun getDiscountForSale(saleId: String): OverallDiscountDocument?

    suspend fun updateDiscountForSale(discount: OverallDiscountDocument)

    suspend fun discountForSale(saleId: String): OverallDiscountRepositoryResponse
}