package com.peyess.salesapp.screen.create_client.basic_info.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun createClientFile(context: Context): File {
    val timeStamp = DateTimeFormatter
        .ofPattern("yyyyMMdd_HHmmss")
        .format(ZonedDateTime.now())

    val storageDir = context.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES
    )

    if (storageDir != null  && !storageDir.exists()) {
        storageDir.mkdirs()
    }
    return File.createTempFile(
        "client_${timeStamp}_",
        ".jpg",
        storageDir,
    )
}