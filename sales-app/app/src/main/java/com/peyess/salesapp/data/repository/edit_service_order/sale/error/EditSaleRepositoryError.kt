package com.peyess.salesapp.data.repository.edit_service_order.sale.error

sealed interface EditSaleRepositoryError {
    val description: String
    val error: Throwable?
}

sealed class InsertSaleError: EditSaleRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateSaleError: EditSaleRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadSaleError: EditSaleRepositoryError {
    data class SaleNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadSaleError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleExistsError: EditSaleRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}
