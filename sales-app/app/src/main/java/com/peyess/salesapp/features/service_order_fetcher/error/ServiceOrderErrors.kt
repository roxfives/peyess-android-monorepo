package com.peyess.salesapp.features.service_order_fetcher.error

sealed interface ServiceOrderErrors {
    val description: String
    val error: Throwable
}

sealed class SaleFetcherReadServiceOrderError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadPurchaseError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPurchaseError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPurchaseError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadPrescriptionError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadPositioningError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPositioningError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadMeasuringError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadMeasuringError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadMeasuringError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadClientError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadClientError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadClientError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class SaleFetcherReadLensError: ServiceOrderErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadLensError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): SaleFetcherReadLensError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddAllClientsError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddAllClientsError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddFramesError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddFramesError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddLensComparisonError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddLensComparisonError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPaymentsError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddPaymentsError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPaymentDiscountError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddPaymentDiscountError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPaymentFeeError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddPaymentFeeError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPositioningError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddPositioningError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddPrescriptionError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddPrescriptionError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddProductPickedError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddProductPickedError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddSaleError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddSaleError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class AddServiceOrderError(): ServiceOrderErrors {
    data class Unexpected(
        override val description: String,
        val throwable: Throwable?,
    ): AddServiceOrderError() {
        override val error = throwable ?: Throwable(description)
    }
}

