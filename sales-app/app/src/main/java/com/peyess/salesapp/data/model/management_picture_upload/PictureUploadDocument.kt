package com.peyess.salesapp.data.model.management_picture_upload

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

data class PictureUploadDocument(
    val id: Long,

    val picture: Uri = Uri.EMPTY,

    val storagePath: String = "",
    val storageName: String = "",

    val hasBeenUploaded: Boolean = false,
    val hasBeenDeleted: Boolean = false,

    val attemptCount: Int = 0,
    val lastAttempt: ZonedDateTime = ZonedDateTime.now(),
)
