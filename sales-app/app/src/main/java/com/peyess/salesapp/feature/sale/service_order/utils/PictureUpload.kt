package com.peyess.salesapp.feature.sale.service_order.utils

import android.net.Uri
import androidx.core.net.toFile
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun buildFilename(uri: Uri, prefix: String = ""): String {
    val fileName = DateTimeFormatter
        .ofPattern("yyyyMMdd_HHmmss")
        .format(ZonedDateTime.now())
    val fileExtension = try {
        uri.toFile().extension
    } catch (e: Exception) {
        "jpeg"
    }

    return "$prefix$fileName.$fileExtension"
}