package com.peyess.salesapp.data.repository.local_sale.lens_comparison

sealed interface LocalComparisonRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalComparisonInsertError: LocalComparisonRepositoryError
data class CreateError(
    override val description: String,
    override val error: Throwable? = null,
): LocalComparisonInsertError

sealed interface LocalComparisonDeletionError: LocalComparisonRepositoryError
data class DeleteError(
    override val description: String,
    override val error: Throwable? = null,
): LocalComparisonDeletionError

sealed interface LocalComparisonUpdateError: LocalComparisonRepositoryError
data class UpdateError(
    override val description: String,
    override val error: Throwable? = null,
): LocalComparisonUpdateError

sealed interface LocalComparisonReadError: LocalComparisonRepositoryError
data class FetchError(
    override val description: String,
    override val error: Throwable? = null,
): LocalComparisonReadError

data class Unexpected(
    override val description: String,
    override val error: Throwable? = null,
): LocalComparisonRepositoryError
