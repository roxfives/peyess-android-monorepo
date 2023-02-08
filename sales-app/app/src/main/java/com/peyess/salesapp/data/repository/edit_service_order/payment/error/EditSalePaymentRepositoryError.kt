package com.peyess.salesapp.data.repository.edit_service_order.payment.error

sealed interface EditSalePaymentDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertSalePaymentError: EditSalePaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertSalePaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateSalePaymentError: EditSalePaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdateSalePaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadSalePaymentError: EditSalePaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadSalePaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}
