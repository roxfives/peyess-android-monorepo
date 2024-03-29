package com.peyess.salesapp.data.model.cache

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.client.Sex
import java.time.ZonedDateTime

@Entity(
    tableName = CacheCreateClientEntity.tableName,
)
data class CacheCreateClientEntity(
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

    @ColumnInfo(name = "is_creating")
    val isCreating: Boolean = false,

    @ColumnInfo(name = "phone_has_whats_app")
    val phoneHasWhatsApp: Boolean = true,
    @ColumnInfo(name = "has_phone_contact")
    val hasPhoneContact: Boolean = false,
    @ColumnInfo(name = "has_accepted_promotional_messages")
    val hasAcceptedPromotionalMessages: Boolean = false,
) {
    companion object {
        const val tableName = "cache_create_client"
    }
}