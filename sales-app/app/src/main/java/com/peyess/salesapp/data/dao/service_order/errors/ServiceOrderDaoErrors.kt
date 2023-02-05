package com.peyess.salesapp.data.dao.service_order.errors

sealed interface ServiceOrderDaoErrors {
    val description: String
    val error: Throwable
}

sealed class ServiceOrderDaoPaginationError: ServiceOrderDaoErrors {
    data class FetchingPageFailed(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderDaoPaginationError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderDaoPaginationError() {
        override val error = throwable ?: Throwable(description)
    }
}

