package com.peyess.salesapp.repository.sale.error

sealed interface SaleRepositoryError {
    val message: String
    val error: Throwable?
}

sealed interface ActiveSaleError: SaleRepositoryError
data class ActiveSaleNotRegistered(
    override val message: String,
    override val error: Throwable? = null,
): ActiveSaleError
data class ActiveSaleNotFound(
    override val message: String,
    override val error: Throwable? = null,
): ActiveSaleError
data class ActiveSaleAlreadyExists(
    override val message: String,
    override val error: Throwable? = null,
): ActiveSaleError

sealed interface ActiveServiceOrderError: SaleRepositoryError
data class ActiveServiceOrderNotRegistered(
    override val message: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError
data class ActiveServiceOrderNotFound(
    override val message: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError
data class ActiveServiceOrderAlreadyExists(
    override val message: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError

data class Unexpected(
    override val message: String,
    override val error: Throwable? = null,
): SaleRepositoryError, ActiveSaleError, ActiveServiceOrderError