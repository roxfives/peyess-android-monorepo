package com.peyess.salesapp.workmanager.utils

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.await
import timber.log.Timber

suspend fun isWorkRunningOrEnqueued(workTag: String, context: Context): Boolean {
    Timber.i("Checking whether worker $workTag is running/enqueued")
    val workManager = WorkManager.getInstance(context)

    val workInfo = workManager.getWorkInfosForUniqueWork(workTag).await()

    return if (workInfo.size > 0) {
        val info = workInfo[0]

        Timber.d("Current work ($workTag): state=${info.state}, id=${info.id}")
        (
            info.state == WorkInfo.State.BLOCKED
                || info.state == WorkInfo.State.ENQUEUED
                || info.state == WorkInfo.State.RUNNING
        )
    } else {
        Timber.i("No worker found with tag $workTag")
        false
    }
}