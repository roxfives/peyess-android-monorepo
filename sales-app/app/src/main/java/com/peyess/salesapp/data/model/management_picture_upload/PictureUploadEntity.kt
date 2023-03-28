package com.peyess.salesapp.data.model.management_picture_upload

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = PictureUploadEntity.tableName)
data class PictureUploadEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "picture")
    val picture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "storage_path")
    val storagePath: String = "",
    @ColumnInfo(name = "storage_name")
    val storageName: String = "",

    @ColumnInfo(name = "has_been_uploaded")
    val hasBeenUploaded: Boolean = false,
    @ColumnInfo(name = "has_been_deleted")
    val hasBeenDeleted: Boolean = false,

    @ColumnInfo(name = "attempt_count")
    val attemptCount: Int = 0,
    @ColumnInfo(name = "last_attempt")
    val lastAttempt: ZonedDateTime = ZonedDateTime.now(),
) {
    companion object {
        const val tableName = "picture_upload_management"
    }
}
