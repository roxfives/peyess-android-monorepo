package com.peyess.salesapp.repository.service_order.error

sealed interface ServiceOrderRepositoryErrors {
    val description: String
    val error: Throwable
}

sealed class ServiceOrderRepositoryPaginationError: ServiceOrderRepositoryErrors {
    data class FetchingPageFailed(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryPaginationError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryPaginationError() {
        override val error = throwable ?: Throwable(description)
    }
}

