package com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error

sealed interface EditLensComparisonDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertLensComparisonError: EditLensComparisonDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertLensComparisonError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateLensComparisonError: EditLensComparisonDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdateLensComparisonError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadLensComparisonError: EditLensComparisonDataRepositoryError {
    data class LensComparisonNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadLensComparisonError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadLensComparisonError() {
        override val error = throwable ?: Throwable(description)
    }
}
