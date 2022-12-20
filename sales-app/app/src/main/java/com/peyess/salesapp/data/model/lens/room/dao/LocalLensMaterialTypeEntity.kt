package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = LocalLensMaterialTypeEntity.tableName)
data class LocalLensMaterialTypeEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "type")
    val type: String = "",
) {
    companion object {
        const val tableName = "local_lenses_material_types"
    }
}
