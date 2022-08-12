package com.peyess.salesapp.utils.file

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Environment
import timber.log.Timber
import java.io.File
import java.util.Date

fun createPrescriptionFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

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

//fun createMeasureFile(context: Context, prefix: String): File {
//
//}