package com.peyess.salesapp.data.dao.client.error

sealed interface ClientDaoErrors {
    val description: String
    val error: Throwable
}

sealed class ReadClientDaoError: ClientDaoErrors {
    data class ClientNotFound(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadClientDaoError() {
        override val error = throwable ?: Throwable(description)
    }

    data class UnexpectedError(
        override val description: String,
        val throwable: Throwable? = null,
    ): ReadClientDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class UpdateClientDaoError: ClientDaoErrors {
    data class UnexpectedError(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateClientDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}

sealed class ExistsClientDaoError: ClientDaoErrors {
    data class UnexpectedError(
        override val description: String,
        val throwable: Throwable? = null,
    ): ExistsClientDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}
