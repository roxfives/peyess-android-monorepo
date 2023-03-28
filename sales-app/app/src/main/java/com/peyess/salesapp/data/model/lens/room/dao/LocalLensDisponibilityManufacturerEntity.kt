package com.peyess.salesapp.data.model.lens.room.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.lens.disponibility.DispManufacturerDocument
import java.time.ZonedDateTime

@Entity(tableName = LocalLensDisponibilityManufacturerEntity.tableName)
data class LocalLensDisponibilityManufacturerEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "shipping_time")
    val shippingTime: Double = 0.0,
) {
    companion object {
        const val tableName = "local_lenses_disponibility_manufacturer"
    }
}
