package com.peyess.salesapp.dao.products.room.local_alt_height

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = LocalAltHeightEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        )
//    ]
)
data class LocalAltHeightEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "lens_id") val lensId: String = "",

    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "name_display") val name_display: String = "",

    @ColumnInfo(name = "value") val value: Double = 0.0,
    @ColumnInfo(name = "observation") val observation: String = "",
) {
    companion object {
        const val tableName = "local_alt_height"
    }
}