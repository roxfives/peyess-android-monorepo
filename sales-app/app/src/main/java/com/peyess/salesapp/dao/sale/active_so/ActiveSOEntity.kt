package com.peyess.salesapp.dao.sale.active_so

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity

@Entity(
    tableName = ActiveSOEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = ActiveSalesEntity::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"],
            onDelete = CASCADE
         )
    ]
)
data class ActiveSOEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "has_prescription") val hasPrescription: Boolean = true,
    @ColumnInfo(name = "sale_id") val saleId: String = "",

    @ColumnInfo(name = "client_name") val clientName: String = "",
    @ColumnInfo(name = "lens_type") val isLensTypeMono: Boolean = false,
) {
    companion object {
        const val tableName = "active_so"
    }
}
