package com.peyess.salesapp.feature.create_client.basic_info.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Environment
import java.io.File
import java.util.Date

fun createClientFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

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