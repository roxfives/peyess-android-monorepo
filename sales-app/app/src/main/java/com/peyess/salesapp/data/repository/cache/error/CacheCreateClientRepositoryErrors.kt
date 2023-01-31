package com.peyess.salesapp.data.repository.cache.error

sealed interface CacheCreateClientRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface CacheCreateClientReadError: CacheCreateClientRepositoryError
data class CacheCreateClientReadSingleError(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientReadError
data class CacheCreateClientNotFoundError(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientReadError

sealed interface CacheCreateClientWriteError: CacheCreateClientRepositoryError
data class CacheCreateClientUpdateError(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientWriteError
data class CacheCreateClientCreateError(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientWriteError
data class CacheCreateClientDeleteError(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientWriteError

data class CacheCreateClientUnexpected(
    override val description: String,
    override val error: Throwable? = null
): CacheCreateClientRepositoryError,
    CacheCreateClientReadError,
    CacheCreateClientWriteError
