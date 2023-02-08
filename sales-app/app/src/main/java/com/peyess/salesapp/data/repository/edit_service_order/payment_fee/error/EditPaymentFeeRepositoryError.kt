package com.peyess.salesapp.data.repository.edit_service_order.payment_fee.error

sealed interface EditPaymentFeeDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertPaymentFeeError: EditPaymentFeeDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertPaymentFeeError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePaymentFeeError: EditPaymentFeeDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdatePaymentFeeError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadPaymentFeeError: EditPaymentFeeDataRepositoryError {
    data class PaymentFeeNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPaymentFeeError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPaymentFeeError() {
        override val error = throwable ?: Throwable(description)
    }
}
