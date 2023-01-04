package com.peyess.salesapp.data.repository.local_sale.frames.error

sealed interface LocalFramesRepositoryError {
    val message: String
    val error: Throwable?
}

sealed interface LocalFramesResponseError: LocalFramesRepositoryError
data class FramesDataNotFound(
    override val message: String, override val error: Throwable? = null,
): LocalFramesResponseError

data class Unexpected(
    override val message: String,
    override val error: Throwable? = null,
): LocalFramesRepositoryError, LocalFramesResponseError