package com.peyess.salesapp.data.repository.edit_service_order.payment.error

sealed interface EditLocalPaymentDataRepositoryError {
    val description: String
    val error: Throwable
}

sealed class InsertLocalPaymentError: EditLocalPaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): InsertLocalPaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class DeleteLocalPaymentError: EditLocalPaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): DeleteLocalPaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateLocalPaymentError: EditLocalPaymentDataRepositoryError {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,    ): UpdateLocalPaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ReadLocalPaymentError: EditLocalPaymentDataRepositoryError {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadLocalPaymentError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadLocalPaymentError() {
        override val error = throwable ?: Throwable(description)
    }
}
