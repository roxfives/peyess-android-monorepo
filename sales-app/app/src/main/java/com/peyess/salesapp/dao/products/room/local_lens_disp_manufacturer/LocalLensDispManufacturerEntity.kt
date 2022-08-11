package com.peyess.salesapp.dao.products.room.local_lens_disp_manufacturer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["lens_id"],
        )
    ]
)
data class LocalLensDispManufacturerEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "shipping_time") val shippingTime: Int = 0,
) {
    companion object {
        const val tableName = "local_disp_manufacturer"
    }
}
