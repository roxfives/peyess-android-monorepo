package com.peyess.salesapp.data.repository.edit_service_order.service_order.error

sealed interface EditServiceOrderRepositoryError {
    val description: String
    val error: Throwable?
}

sealed class InsertServiceOrderError: EditServiceOrderRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateServiceOrderError: EditServiceOrderRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadServiceOrderError: EditServiceOrderRepositoryError {
    data class ServiceOrderNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}

