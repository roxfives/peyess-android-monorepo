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

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): ClientRepositoryError, ClientReadError