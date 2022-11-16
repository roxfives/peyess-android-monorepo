package com.peyess.salesapp.data.dao.products_table_state

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = ProductTableStateEntity.tableName,
)
data class ProductTableStateEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,

    @ColumnInfo(name = "has_updated")
    val hasUpdated: Boolean = false,
    @ColumnInfo(name = "has_updated_failed")
    val hasUpdateFailed: Boolean = false,
    @ColumnInfo(name = "is_updating")
    val isUpdating: Boolean = false,
) {
    companion object {
        const val tableName = "products_state_table"
    }
}
