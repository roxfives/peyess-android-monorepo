package com.peyess.salesapp.data.repository.local_sale.measuring.error

sealed interface LocalMeasuringRepositoryError {
    val description: String
    val error: Throwable?
}

sealed interface LocalMeasuringResponseError: LocalMeasuringRepositoryError
data class MeasuringDataNotFound(
    override val description: String, override val error: Throwable? = null,
): LocalMeasuringResponseError
