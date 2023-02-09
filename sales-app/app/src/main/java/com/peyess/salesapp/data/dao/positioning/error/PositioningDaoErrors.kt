package com.peyess.salesapp.data.dao.positioning.error

sealed interface PositioningDaoErrors {
    val description: String
    val error: Throwable
}

sealed class ReadPositioningDaoError: PositioningDaoErrors {
    data class NotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningDaoError() {
        override val error = throwable ?: Throwable(description)
    }

    data class Unexpected(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadPositioningDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}
