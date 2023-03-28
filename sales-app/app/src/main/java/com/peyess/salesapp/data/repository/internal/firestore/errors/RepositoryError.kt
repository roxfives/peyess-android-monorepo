package com.peyess.salesapp.data.repository.internal.firestore.errors

sealed interface RepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface ReadError: RepositoryError
data class FetchDataError(
    override val description: String,
    override val error: Throwable?,
): ReadError

sealed interface WriteError: RepositoryError
data class UploadError(
    override val description: String,
    override val error: Throwable?,
): WriteError

data class NetworkError(
    override val description: String,
    override val error: Throwable?,
): ReadError, WriteError

sealed interface PaginationError: RepositoryError
data class CreatePaginatorError(
    override val description: String,
    override val error: Throwable?,
): PaginationError
data class FetchPageError(
    override val description: String,
    override val error: Throwable?,
): PaginationError, ReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable?,
): RepositoryError, PaginationError
