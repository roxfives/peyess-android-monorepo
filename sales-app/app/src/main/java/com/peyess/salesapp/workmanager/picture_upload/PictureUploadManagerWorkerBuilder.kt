package com.peyess.salesapp.workmanager.picture_upload

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

fun enqueuePictureUploadManagerWorker(
    context: Context,
    uploadEntryId: Long,
    workPolicy: ExistingWorkPolicy = ExistingWorkPolicy.KEEP,
) {
    val inputData = Data.Builder()
        .putLong(uploadEntryIdKey, uploadEntryId)
        .build()

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(false)
        .setRequiresStorageNotLow(false)
        .build()

    val uploadWorkRequest = OneTimeWorkRequestBuilder<PictureUploadManagerWorker>()
        .setInputData(inputData)
        .setConstraints(constraints)
        .build()

    WorkManager
        .getInstance(context)
        .enqueueUniqueWork(
            PictureUploadManagerWorker.workerTag,
            workPolicy,
            uploadWorkRequest,
        )
}