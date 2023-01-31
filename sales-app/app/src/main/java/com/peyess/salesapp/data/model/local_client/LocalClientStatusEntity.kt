package com.peyess.salesapp.data.model.local_client

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity(tableName = LocalClientStatusEntity.tableName)
data class LocalClientStatusEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "has_been_initiated")
    val hasBeenInitiated: Boolean = false,

    @ColumnInfo(name = "is_updating")
    val isUpdating: Boolean = false,

    @ColumnInfo(name = "has_latest_download_failed")
    val hasLatestDownloadFailed: Boolean = false,
    @ColumnInfo(name = "has_latest_upload_failed")
    val hasLatestUploadFailed: Boolean = false,

    @ColumnInfo(name = "latest_update")
    val latestDownload: ZonedDateTime = ZonedDateTime
        .ofInstant(Instant.EPOCH, ZoneId.systemDefault()),
    @ColumnInfo(name = "latest_upload")
    val latestUpload: ZonedDateTime = ZonedDateTime
        .ofInstant(Instant.EPOCH, ZoneId.systemDefault()),
    ) {
    companion object {
        const val tableName = "local_client_status"
    }
}
