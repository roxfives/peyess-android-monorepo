package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = LocalLensAltHeightEntity.tableName)
data class LocalLensAltHeightEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "name_display")
    val nameDisplay: String = "",

    @ColumnInfo(name = "value")
    val value: Double = 0.0,

    @ColumnInfo(name = "observation")
    val observation: String = "",
) {
    companion object {
        const val tableName = "local_lenses_alt_height"
    }
}
