package com.peyess.salesapp.database.room.gambeta

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = GambetaEntity.tableName,
)
data class GambetaEntity(
    @PrimaryKey val id: Int = 0,
    @ColumnInfo(name = "has_updated")
    val hasUpdated: Boolean = false,
    @ColumnInfo(name = "is_updating")
    val isUpdating: Boolean = false,
) {
    companion object {
        const val tableName = "gambeta_table"
    }
}
