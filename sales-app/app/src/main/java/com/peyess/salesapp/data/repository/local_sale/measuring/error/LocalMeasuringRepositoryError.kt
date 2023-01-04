package com.peyess.salesapp.data.repository.local_sale.measuring.error

sealed interface LocalMeasuringRepositoryError {
    val message: String
    val error: Throwable?
}

sealed interface LocalMeasuringResponseError: LocalMeasuringRepositoryError
data class MeasuringDataNotFound(
    override val message: String, override val error: Throwable? = null,
): LocalMeasuringResponseError
