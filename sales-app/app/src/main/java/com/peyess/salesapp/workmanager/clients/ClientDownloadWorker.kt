package com.peyess.salesapp.workmanager.clients

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.getOrElse
import arrow.fx.coroutines.Schedule
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.client.toLocalClientDocument
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.local_client.LocalClientStatusDocument
import com.peyess.salesapp.data.repository.client.ClientPaginationResponse
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.workmanager.clients.error.ClientDownloadError
import com.peyess.salesapp.workmanager.clients.error.ClientWorkerUpdateStatusError
import com.peyess.salesapp.workmanager.clients.utils.buildQueryForClients
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

const val notificationId = 21

@HiltWorker
class ClientDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesApplication: SalesApplication,
    private val clientRepository: ClientRepository,
    private val localClientRepository: LocalClientRepository,
): CoroutineWorker(context, workerParams) {

    private fun createNotification() {
        val context = salesApplication as Context
        val packageManager = context.packageManager

        val launchIntent = packageManager.getLaunchIntentForPackage(BuildConfig.APPLICATION_ID)
        val pendingIntent = PendingIntent
            .getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "clients_sync_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(salesApplication.stringResource(R.string.notification_title_download_clients))
            .setContentText(salesApplication.stringResource(R.string.notification_description_download_clients))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(false)
            .setOngoing(true)
            .setProgress(0, 100, true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }

    private fun dismissNotification() {
        val context = salesApplication as Context

        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
        }
    }

    private suspend fun updateStatus(
        isUpdating: Boolean,
        latestDownload: ZonedDateTime? = null,
        hasBeenInitiated: Boolean? = null,
    ): Either<ClientDownloadError, LocalClientStatusDocument> {
        val response = localClientRepository.clientStatus()

        val status = response.fold(
            ifLeft = {
                Timber.e("Error while reading client status: $it")
                LocalClientStatusDocument()
            },

            ifRight = { it }
        )

        val update = status.copy(
            isUpdating = isUpdating,
            latestDownload = latestDownload ?: status.latestDownload,
            hasBeenInitiated = hasBeenInitiated ?: status.hasBeenInitiated,
        )

        return localClientRepository.updateClientStatus(update).mapLeft {
            ClientWorkerUpdateStatusError(
                description = "Error while updating client status with $isUpdating",
                error = it.error
            )
        }.map { update }
    }

    private suspend fun populateClientsDatabase(clients: List<ClientDocument>) {
        clients.forEach { client ->
            val document = client.toLocalClientDocument(
                hasBeenUploaded = true,
                hasBeenDownloaded = true,

                downloadedAt = ZonedDateTime.now(),
                uploadedAt = client.updated,
            )

            localClientRepository.insertClient(document).fold(
                ifLeft = {
                    Timber.e("Error while inserting client: $it")
                },

                ifRight = {
                    Timber.d("Client inserted: $it")
                }
            )
        }
    }

    private suspend fun downloadClients(
        query: PeyessQuery,
        queryConfig: SimplePaginatorConfig,
    ) = Schedule.doWhile<ClientPaginationResponse> {
            it.getOrElse { emptyList() }.isNotEmpty()
        }.repeat {
           clientRepository.paginateData(query, queryConfig).tap {
                populateClientsDatabase(it)
            }
        }

    private suspend fun findLatestUpdatedClient(): ZonedDateTime {
        return localClientRepository.latestClientUpdated()
            .fold(
                ifLeft = {
                    val instant = Instant.EPOCH

                    ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
                },

                ifRight = { it.updated }
            )
    }

    override suspend fun doWork(): Result {
        createNotification()

        val isInitiating = inputData.getBoolean(isInitiatingKey, false)

        val latestUpdated = findLatestUpdatedClient()
        val result = updateStatus(isUpdating = true).flatMap { status ->
            if (isInitiating && status.hasBeenInitiated) {
                Timber.i("Client download has already been initiated")
                return@flatMap Either.Right(status)
            } else {
                val queryPair = buildQueryForClients(salesApplication, latestUpdated)

                Timber.i("Downloading clients with query: $queryPair")
                downloadClients(queryPair.first, queryPair.second).map { status }
            }
        }.flatMap {
            updateStatus(
                isUpdating = false,
                latestDownload = ZonedDateTime.now(),
                hasBeenInitiated = isInitiating || it.hasBeenInitiated
            )
        }.fold(
            ifLeft = {
                Timber.e("Error while downloading clients: $it", it)

                Result.retry()
            },

            ifRight = {
                Timber.i("Finished downloading clients successfully")

                Result.success()
            },
        )

        dismissNotification()

        return result
    }

    companion object {
        const val oneTimeWorkerTag = "TAG_ClientDownloadWorker_OneTime"
        const val periodicWorkerTag = "TAG_ClientDownloadWorker_Periodic"
    }
}