package com.peyess.salesapp.data.repository.local_sale.client_picked.error

sealed interface ClientPickedRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface ClientPickedReadError: ClientPickedRepositoryError
data class ClientPickedNotFound(
    override val description: String,
    override val error: Throwable? = null,
): ClientPickedReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): ClientPickedRepositoryError, ClientPickedReadError