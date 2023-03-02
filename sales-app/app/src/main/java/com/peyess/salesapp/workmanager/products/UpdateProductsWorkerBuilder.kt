package com.peyess.salesapp.workmanager.products

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.peyess.salesapp.workmanager.utils.isWorkRunningOrEnqueued

suspend fun enqueueProductUpdateWorker(
    context: Context,
    workPolicy: ExistingWorkPolicy = ExistingWorkPolicy.REPLACE,
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

        uploadWorkRequest = OneTimeWorkRequestBuilder<UpdateProductsWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                UpdateProductsWorker.workerTag,
                workPolicy,
                uploadWorkRequest,
            )
    }
}