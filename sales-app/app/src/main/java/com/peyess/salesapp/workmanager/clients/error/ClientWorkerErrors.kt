package com.peyess.salesapp.workmanager.clients.error

sealed interface ClientWorkerError {
    val description: String
    val error: Throwable?
}

sealed interface ClientDownloadError: ClientWorkerError
data class ClientWorkerReadStatusError(
    override val description: String,
    override val error: Throwable? = null,
): ClientDownloadError
data class ClientWorkerUpdateStatusError(
    override val description: String,
    override val error: Throwable? = null,
): ClientDownloadError
data class ClientWorkerReadError(
    override val description: String,
    override val error: Throwable? = null,
): ClientDownloadError

data class ClientWorkerUnexpectedError(
    override val description: String,
    override val error: Throwable? = null,
): ClientWorkerError, ClientDownloadError