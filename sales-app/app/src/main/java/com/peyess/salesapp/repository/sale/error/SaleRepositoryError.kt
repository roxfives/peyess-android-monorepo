package com.peyess.salesapp.repository.sale.error

sealed interface SaleRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface ActiveSaleError: SaleRepositoryError
data class ActiveSaleNotRegistered(
    override val description: String,
    override val error: Throwable? = null,
): ActiveSaleError
data class ActiveSaleNotFound(
    override val description: String,
    override val error: Throwable? = null,
): ActiveSaleError
data class ActiveSaleAlreadyExists(
    override val description: String,
    override val error: Throwable? = null,
): ActiveSaleError
data class ActiveSaleNotCanceled(
    override val description: String,
    override val error: Throwable? = null,
): ActiveSaleError

sealed interface ActiveServiceOrderError: SaleRepositoryError
data class ActiveServiceOrderNotRegistered(
    override val description: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError
data class ActiveServiceOrderNotFound(
    override val description: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError
data class ActiveServiceOrderAlreadyExists(
    override val description: String,
    override val error: Throwable? = null,
): ActiveServiceOrderError

sealed interface ProductPickedError: SaleRepositoryError
data class ProductPickedNotFound(
    override val description: String,
    override val error: Throwable? = null,
): ProductPickedError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): SaleRepositoryError, ActiveSaleError, ActiveServiceOrderError, ProductPickedError
