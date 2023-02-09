package com.peyess.salesapp.data.repository.local_sale.frames.error

sealed interface LocalFramesRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalFramesResponseError: LocalFramesRepositoryError
data class FramesDataNotFound(
    override val description: String,
    override val error: Throwable? = null,
): LocalFramesResponseError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): LocalFramesRepositoryError, LocalFramesResponseError
