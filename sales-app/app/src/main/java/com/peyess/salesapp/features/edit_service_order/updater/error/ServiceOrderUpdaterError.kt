package com.peyess.salesapp.features.edit_service_order.updater.error

sealed interface ServiceOrderUpdaterErrors {
    val description: String
    val error: Throwable
}

sealed class GenerateSaleDataError: ServiceOrderUpdaterErrors {
    data class FetchLocalServiceOrderError(
        override val description: String = "Error fetching local service order",
        val throwable: Throwable? = null,
    ): GenerateSaleDataError() {
        override val error = throwable ?: Throwable(description)
    }

    data class ClientNotFound(
        override val description: String = "Error fetching client",
        val throwable: Throwable? = null,
    ): GenerateSaleDataError() {
        override val error = throwable ?: Throwable(description)
    }

    data class FetchClientUnexpectedError(
        override val description: String = "Error fetching client",
        val throwable: Throwable? = null,
    ): GenerateSaleDataError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String = "Unexpected error",
        val throwable: Throwable? = null,
    ): GenerateSaleDataError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class GeneratePrescriptionDataError: ServiceOrderUpdaterErrors {
    data class Unexpected(
        override val description: String = "Unexpected error",
        val throwable: Throwable? = null,
    ): GeneratePrescriptionDataError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateServiceOrderError: ServiceOrderUpdaterErrors {
    data class Error(
        override val description: String = "Error updating service order",
        val throwable: Throwable? = null,
    ): UpdateServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}
