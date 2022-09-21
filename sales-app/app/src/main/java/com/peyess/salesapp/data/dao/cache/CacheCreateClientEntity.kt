package com.peyess.salesapp.data.dao.cache

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.client.Sex
import java.time.OffsetDateTime
import java.util.Date

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
    val birthday: OffsetDateTime = OffsetDateTime.now(),

    @ColumnInfo(name = "document")
    val document: String = "",

    @ColumnInfo(name = "sex")
    val sex: Sex = Sex.Other,

    @ColumnInfo(name = "email")
    val email: String = "",

    @ColumnInfo(name = "short_address")
    val shortAddress: String = "",
) {
    companion object {
        const val tableName = "cache_create_client"
    }
}