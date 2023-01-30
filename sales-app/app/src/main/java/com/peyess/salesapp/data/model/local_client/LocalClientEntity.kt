package com.peyess.salesapp.data.model.local_client

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.client.Sex
import java.time.ZonedDateTime

@Entity(tableName = LocalClientEntity.tableName)
data class LocalClientEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "name_display")
    val nameDisplay: String = "",

    @ColumnInfo(name = "picture")
    val picture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "birthday")
    val birthday: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "document")
    val document: String = "",
    @ColumnInfo(name = "sex")
    val sex: Sex = Sex.Unknown,

    @ColumnInfo(name = "zip_code")
    val zipCode: String = "",
    @ColumnInfo(name = "street")
    val street: String = "",
    @ColumnInfo(name = "house_number")
    val houseNumber: String = "",
    @ColumnInfo(name = "complement")
    val complement: String = "",
    @ColumnInfo(name = "neighborhood")
    val neighborhood: String = "",
    @ColumnInfo(name = "city")
    val city: String = "",
    @ColumnInfo(name = "state")
    val state: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "phone")
    val phone: String = "",
    @ColumnInfo(name = "cellphone")
    val cellphone: String = "",
    @ColumnInfo(name = "whatsapp")
    val whatsapp: String = "",

    @ColumnInfo(name = "doc version")
    val doc_version: Int = 0,
    @ColumnInfo(name = "is editable")
    val is_editable: Boolean = false,

    @ColumnInfo(name = "created")
    val created: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "created_by")
    val createdBy: String = "",
    @ColumnInfo(name = "create_allowed_by")
    val createAllowedBy: String = "",

    @ColumnInfo(name = "updated")
    val updated: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "updated_by")
    val updatedBy: String = "",
    @ColumnInfo(name = "update_allowed_by")
    val updateAllowedBy: String = "",

    @ColumnInfo(name = "has_been_uploaded")
    val hasBeenUploaded: Boolean = false,
    @ColumnInfo(name = "has_been_downloaded")
    val hasBeenDownloaded: Boolean = false,

    @ColumnInfo(name = "downloaded_at")
    val downloadedAt: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "uploaded_at")
    val uploadedAt: ZonedDateTime = ZonedDateTime.now(),
) {
    companion object {
        const val tableName = "local_client"
    }
}
