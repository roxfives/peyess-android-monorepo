package com.peyess.salesapp.workmanager.clients

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import kotlin.time.toJavaDuration

const val isInitiatingKey = "isInitiatingKey"

private fun buildWorkerConstraints(): Constraints {
    return Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(false)
        .setRequiresStorageNotLow(false)
        .build()
}

private fun buildPeriodicWorkRequest(
    constraints: Constraints,
    inputData: Data,
): PeriodicWorkRequest {
    val interval = 12.toDuration(DurationUnit.HOURS).toJavaDuration()
    val flex = 12.toDuration(DurationUnit.HOURS).toJavaDuration()
    val backOffInterval = 30.toDuration(DurationUnit.SECONDS).toJavaDuration()

    return PeriodicWorkRequestBuilder<ClientDownloadWorker>(interval, flex)
        .setConstraints(constraints)
        .setInputData(inputData)
        .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, backOffInterval)
        .build()
}

private fun buildOneTimeWorkRequest(constraints: Constraints, inputData: Data): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<ClientDownloadWorker>()
        .setConstraints(constraints)
        .setInputData(inputData)
        .build()
}

private fun buildInputData(isInitiating: Boolean): Data {
    return Data.Builder()
        .putBoolean(isInitiatingKey, isInitiating)
        .build()
}

fun enqueueOneTimeClientDownloadWorker(
    context: Context,
    forceSync: Boolean = false,
    workPolicy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE,
) {
    val inputData = buildInputData(isInitiating = !forceSync)
    val constraints = buildWorkerConstraints()
    val uploadWorkRequest = buildOneTimeWorkRequest(constraints, inputData)

    WorkManager
        .getInstance(context)
        .enqueueUniqueWork(
            ClientDownloadWorker.oneTimeWorkerTag,
            workPolicy,
            uploadWorkRequest,
        )
}

fun enqueuePeriodicClientDownloadWorker(
    context: Context,
    workPolicy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.REPLACE,
) {
    val inputData = buildInputData(isInitiating = false)
    val constraints = buildWorkerConstraints()
    val uploadWorkRequest = buildPeriodicWorkRequest(constraints, inputData)

    WorkManager
        .getInstance(context)
        .enqueueUniquePeriodicWork(
            ClientDownloadWorker.periodicWorkerTag,
            workPolicy,
            uploadWorkRequest,
        )
}