package com.peyess.salesapp.dao.products.room.local_coloring_display

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity

@Entity(
    tableName = LocalColoringDisplayEntity.tableName,
//    foreignKeys = [ForeignKey(
//        entity = LocalColoringEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["coloring_id"],
//        onDelete = CASCADE,
//    )]
)
data class LocalColoringDisplayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "coloring_id") val coloring_id: String = "",
    @ColumnInfo(name = "color_code") val colorCode: String = "",
) {
    companion object {
        const val tableName = "coloring_display"
    }
}
