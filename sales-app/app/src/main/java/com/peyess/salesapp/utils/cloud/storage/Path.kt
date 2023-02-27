package com.peyess.salesapp.utils.cloud.storage

private const val storagePathDelimiter = "/"

fun appendStoragePaths(paths: List<String>): String {
    return paths.joinToString(separator = storagePathDelimiter)
}
