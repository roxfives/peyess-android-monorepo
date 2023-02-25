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

sealed class ServiceOrderRepositoryFetchError: ServiceOrderRepositoryErrors {
    data class ServiceOrderNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryFetchError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryFetchError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ServiceOrderRepositoryUpdateError: ServiceOrderRepositoryErrors {
    data class ServiceOrderNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryUpdateError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ServiceOrderRepositoryUpdateError() {
        override val error = throwable ?: Throwable(description)
    }
}
