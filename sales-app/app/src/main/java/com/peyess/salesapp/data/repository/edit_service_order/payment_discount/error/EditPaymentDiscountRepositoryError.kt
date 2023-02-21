package com.peyess.salesapp.data.repository.edit_service_order.payment_discount.error

sealed interface EditPaymentDiscountDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertPaymentDiscountError: EditPaymentDiscountDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertPaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdatePaymentDiscountError: EditPaymentDiscountDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdatePaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadPaymentDiscountError: EditPaymentDiscountDataRepositoryError {
    data class PaymentDiscountNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeletePaymentDiscountError: EditPaymentDiscountDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeletePaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }
}
