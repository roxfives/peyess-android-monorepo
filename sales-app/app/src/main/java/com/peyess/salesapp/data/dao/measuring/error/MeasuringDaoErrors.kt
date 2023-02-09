package com.peyess.salesapp.data.dao.measuring.error

sealed interface MeasuringDaoErrors {
    val description: String
    val error: Throwable
}

sealed class ReadMeasuringDaoError: MeasuringDaoErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadMeasuringDaoError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadMeasuringDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}
