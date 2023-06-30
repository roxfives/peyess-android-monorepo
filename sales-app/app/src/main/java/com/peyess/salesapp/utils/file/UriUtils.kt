package com.peyess.salesapp.utils.file

import android.net.Uri
import android.webkit.URLUtil


fun Uri.isLocalFile(): Boolean {
    val asString = this.toString()

    return URLUtil.isValidUrl(asString)
            && (URLUtil.isFileUrl(asString) || URLUtil.isContentUrl(asString))
}

fun Uri.isNotLocalFile(): Boolean {
    return !isLocalFile()
}