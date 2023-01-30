package com.peyess.salesapp.data.model.local_client

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

data class LocalClientStatusDocument(
    val id: Long = 0L,

    val hasLatestDownloadFailed: Boolean = false,
    val hasLatestUploadFailed: Boolean = false,

    val latestDownload: ZonedDateTime = ZonedDateTime
        .ofInstant(Instant.EPOCH, ZoneId.systemDefault()),
    val latestUpload: ZonedDateTime = ZonedDateTime
        .ofInstant(Instant.EPOCH, ZoneId.systemDefault()),
)

