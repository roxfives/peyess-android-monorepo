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

sealed class GeneratePositioningDataError: ServiceOrderUpdaterErrors {
    data class Unexpected(
        override val description: String = "Unexpected error",
        val throwable: Throwable? = null,
    ): GeneratePositioningDataError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class GenerateMeasuringDataError: ServiceOrderUpdaterErrors {
    data class Unexpected(
        override val description: String = "Unexpected error",
        val throwable: Throwable? = null,
    ): GenerateMeasuringDataError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateSaleError: ServiceOrderUpdaterErrors {
    data class Unexpected(
        override val description: String = "Error updating service order",
        val throwable: Throwable? = null,
    ): UpdateSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPictureToUploadError: ServiceOrderUpdaterErrors {
    data class Unexpected(
        override val description: String = "Error adding picture to upload",
        val throwable: Throwable? = null,
    ): AddPictureToUploadError() {
        override val error = throwable ?: Throwable(description)
    }
}
