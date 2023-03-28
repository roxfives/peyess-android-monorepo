package com.peyess.salesapp.data.repository.local_sale.positioning.error

sealed interface LocalPositioningRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalPositioningWriteError: LocalPositioningRepositoryError
data class InsertLocalPositioningError(
    override val description: String,
    override val error: Throwable? = null,
): LocalPositioningReadError

sealed interface LocalPositioningReadError: LocalPositioningRepositoryError
data class LocalPositioningNotFoundError(
    override val description: String,
    override val error: Throwable? = null,
): LocalPositioningReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): LocalPositioningRepositoryError, LocalPositioningReadError
