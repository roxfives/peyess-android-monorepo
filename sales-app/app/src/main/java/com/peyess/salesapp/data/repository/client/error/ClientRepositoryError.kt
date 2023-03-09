package com.peyess.salesapp.data.repository.client.error

sealed interface ClientRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface ClientReadError: ClientRepositoryError
data class ClientNotFound(
    override val description: String,
    override val error: Throwable? = null,
): ClientReadError

data class ClientRepositoryUnexpectedError(
    override val description: String,
    override val error: Throwable? = null,
): ClientRepositoryError, ClientReadError

sealed class UploadClientRepositoryError: ClientRepositoryError {
    data class Unexpected(
        override val description: String,
        override val error: Throwable? = null,
    ): UploadClientRepositoryError()
}

sealed class UpdateClientRepositoryError: ClientRepositoryError {
    data class NotFound(
        override val description: String,
        override val error: Throwable? = null,
    ): UpdateClientRepositoryError()

    data class Unexpected(
        override val description: String,
        override val error: Throwable? = null,
    ): UpdateClientRepositoryError()
}

sealed class ExistsClientRepositoryError: ClientRepositoryError {
    data class Unexpected(
        override val description: String,
        override val error: Throwable? = null,
    ): ExistsClientRepositoryError()
}
