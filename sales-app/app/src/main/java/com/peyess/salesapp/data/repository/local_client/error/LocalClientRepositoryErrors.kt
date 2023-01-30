package com.peyess.salesapp.data.repository.local_client.error

sealed interface LocalClientRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalClientRepositoryFetchStatusError: LocalClientRepositoryError
data class LocalClientRepositoryReadStatusError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryFetchStatusError
data class LocalClientRepositoryStatusNotFoundError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryFetchStatusError

sealed interface LocalClientRepositoryStatusWriteError: LocalClientRepositoryError
data class LocalClientRepositoryStatusInsertError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryStatusWriteError
data class LocalClientRepositoryStatusUpdateError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryStatusWriteError

sealed interface LocalClientRepositoryFetchError: LocalClientRepositoryError
data class LocalClientRepositoryReadError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryFetchError
data class LocalClientNotFoundError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryFetchError


sealed interface LocalClientRepositoryPagingError: LocalClientRepositoryError
data class LocalClientRepositoryCreatePagingSourceError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryPagingError


sealed interface LocalClientRepositoryWriteError: LocalClientRepositoryError
data class LocalClientRepositoryInsertError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryWriteError
data class LocalClientRepositoryUpdateError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryWriteError
data class LocalClientRepositoryDeleteError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryWriteError

data class UnexpectedLocalClientRepositoryError(
    override val description: String,
    override val error: Throwable?,
): LocalClientRepositoryError,
    LocalClientRepositoryFetchError,
    LocalClientRepositoryPagingError,
    LocalClientRepositoryWriteError,
    LocalClientRepositoryFetchStatusError,
    LocalClientRepositoryStatusWriteError
