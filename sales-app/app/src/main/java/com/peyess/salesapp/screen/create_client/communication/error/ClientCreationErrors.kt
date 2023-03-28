package com.peyess.salesapp.screen.create_client.communication.error

sealed interface ClientCreationError {
    val description: String
    val error: Throwable
}

sealed class SearchExistingClientError: ClientCreationError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SearchExistingClientError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateClientError: ClientCreationError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateClientError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateClientError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class CreateClientError: ClientCreationError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): CreateClientError() {
        override val error = throwable ?: Throwable(description)
    }
}
