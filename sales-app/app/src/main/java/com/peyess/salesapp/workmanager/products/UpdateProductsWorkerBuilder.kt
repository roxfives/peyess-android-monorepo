package com.peyess.salesapp.workmanager.products

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.peyess.salesapp.workmanager.utils.isWorkRunningOrEnqueued
import java.util.concurrent.TimeUnit

suspend fun enqueueProductUpdateWorker(
    context: Context,
    workPolicy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE,
    executeRightAway: Boolean = false,
    forceExecution: Boolean = false,
) {
    val workTag = UpdateProductsWorker.workerTag

    val inputData: Data
    val constraints: Constraints
    val uploadWorkRequest: OneTimeWorkRequest

    val runWorker = forceExecution || !isWorkRunningOrEnqueued(workTag, context)

    if (runWorker) {
        inputData = Data.Builder()
            .putBoolean(forceUpdateKey, forceExecution)
            .build()

        constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .setRequiresStorageNotLow(false)
            .build()

        val uploadWorkRequestBuilder = OneTimeWorkRequestBuilder<UpdateProductsWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS,
            )
        if (!executeRightAway) {
            uploadWorkRequestBuilder.setInitialDelay(5, TimeUnit.SECONDS)
        }

        uploadWorkRequest = uploadWorkRequestBuilder.build()
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                UpdateProductsWorker.workerTag,
                workPolicy,
                uploadWorkRequest,
            )
    }
}

fun cancelAnyProductUpdateWorker(context: Context) {
    WorkManager.getInstance(context).cancelAllWorkByTag(UpdateProductsWorker.workerTag)
}
