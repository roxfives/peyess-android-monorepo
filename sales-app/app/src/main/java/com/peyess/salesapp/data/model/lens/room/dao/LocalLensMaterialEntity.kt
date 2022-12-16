package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalLensMaterialEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensMaterialCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = NO_ACTION,
        ),
    ]
)
data class LocalLensMaterialEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "priority")
    val priority: Int = 0,

    @ColumnInfo(name = "store_priority")
    val storePriority: Int = 0,

    @ColumnInfo(name = "n")
    val n: Double = 0.0,

    @ColumnInfo(name = "observation")
    val observation: String = "",

    @ColumnInfo(name = "category_id")
    val categoryId: String = "",

    @ColumnInfo(name = "category")
    val category: String = "",
) {
    companion object {
        const val tableName = "local_lens_material"
    }
}
