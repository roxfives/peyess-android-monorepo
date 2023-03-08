package com.peyess.salesapp.data.dao.client_legal.error

sealed interface ClientLegalDaoErrors {
    val description: String
    val error: Throwable
}

sealed class UpdateClientLegalDaoError: ClientLegalDaoErrors {
    data class UnexpectedError(
        override val description: String,
        val throwable: Throwable? = null,
    ): UpdateClientLegalDaoError() {
        override val error = throwable ?: Throwable(description)
    }
}