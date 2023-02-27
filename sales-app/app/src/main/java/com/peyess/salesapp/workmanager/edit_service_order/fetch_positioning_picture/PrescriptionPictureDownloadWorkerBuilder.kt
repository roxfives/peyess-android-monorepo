package com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

fun enqueuePositioningPictureDownloadWorker(
    context: Context,
    positioningId: String,
) {
    val inputData = Data.Builder()
        .putString(positioningIdEntryKey, positioningId)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(false)
        .setRequiresCharging(false)
        .setRequiresStorageNotLow(true)
        .build()

    val uploadWorkRequest = OneTimeWorkRequestBuilder<PositioningPictureDownloadWorker>()
        .setInputData(inputData)
        .setConstraints(constraints)
        .build()

    WorkManager
        .getInstance(context)
        .enqueue(uploadWorkRequest)
}