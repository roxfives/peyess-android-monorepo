package com.peyess.salesapp.data.repository.discount.error

sealed interface OverallDiscountRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface OverallDiscountReadError: OverallDiscountRepositoryError
data class OverallDiscountNotFound(
    override val description: String,
    override val error: Throwable? = null,
): OverallDiscountReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): OverallDiscountRepositoryError, OverallDiscountReadError