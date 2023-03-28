package com.peyess.salesapp.data.repository.edit_service_order.product_picked.error

sealed interface EditProductPickedRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertProductPickedError: EditProductPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateProductPickedError: EditProductPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadProductPickedError: EditProductPickedRepositoryError {
    data class ProductPickedNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeleteProductPickedError: EditProductPickedRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeleteProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}
