package com.peyess.salesapp.data.dao.purchase.error

sealed interface PurchaseDaoError {
    val description: String
    val error: Throwable
}

sealed class UpdatePurchaseDaoError: PurchaseDaoError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdatePurchaseDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}
