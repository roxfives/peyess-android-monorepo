package com.peyess.salesapp.utils.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import timber.log.Timber
import java.io.File
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun createPrescriptionFile(context: Context): File {
    val timeStamp = DateTimeFormatter
        .ofPattern("yyyyMMdd_HHmmss")
        .format(ZonedDateTime.now())

    val storageDir = context.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES
    )

    if (storageDir != null  && !storageDir.exists()) {
        storageDir.mkdirs()
        Timber.i("The external dir is ${storageDir.absolutePath}")
    }
    return File.createTempFile(
        "prescription_${timeStamp}_",
        ".jpg",
        storageDir,
    )
}

fun deleteFile(uri: Uri) {
    val file = uri.path?.let { File(it) }

    if (file != null && file.exists()) {
        if (file.delete()) {
            Timber.i("File deleted: ${uri.path}")
        } else {
            Timber.i("Failed to delete file: ${uri.path}")
        }
    }

}

fun createPrintFile(context: Context): File {
    val timeStamp = DateTimeFormatter
        .ofPattern("yyyyMMdd_HHmmss")
        .format(ZonedDateTime.now())

    val storageDir = context.getExternalFilesDir(
        Environment.DIRECTORY_DOCUMENTS
    )

    if (storageDir != null  && !storageDir.exists()) {
        storageDir.mkdirs()
        Timber.i("The external dir is ${storageDir.absolutePath}")
    }
    return File.createTempFile(
        "print_test_${timeStamp}_",
        ".pdf",
        storageDir,
    )
}
