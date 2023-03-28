package com.peyess.salesapp.data.repository.edit_service_order.client_picked.error

sealed interface EditClientPickedRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertClientPickedError: EditClientPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertClientPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateClientPickedError: EditClientPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdateClientPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadClientPickedError: EditClientPickedRepositoryError {
    data class ClientPickedNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadClientPickedError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadClientPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeleteClientPickedError: EditClientPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeleteClientPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}
