package com.peyess.salesapp.dao.sale.active_so

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity

sealed class LensTypeCategoryName {
    object Near: LensTypeCategoryName()
    object Far: LensTypeCategoryName()
    object Multi: LensTypeCategoryName()
    object None: LensTypeCategoryName()

    fun toName() = fromType(this)

    companion object {
        fun fromName(name: String?): LensTypeCategoryName {
            return when(name?.lowercase() ?: "") {
                "perto" -> Near
                "longe" -> Far
                "multifocal" -> Multi
                else -> None
            }
        }

        fun fromType(type: LensTypeCategoryName?): String {
            return when(type) {
                Near -> "perto"
                Far -> "longe"
                Multi -> "multifocal"
                else -> "nonw"
            }
        }
    }
}

@Entity(
    tableName = ActiveSOEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSalesEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["sale_id"],
//            onDelete = CASCADE
//         )
//    ]
)
data class ActiveSOEntity(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "has_prescription")
    val hasPrescription: Boolean = true,
    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "client_name")
    val clientName: String = "",
    @ColumnInfo(name = "lens_type_name")
    val lensTypeCategoryName: LensTypeCategoryName = LensTypeCategoryName.None,
    @ColumnInfo(name = "is_lens_type_mono")
    val isLensTypeMono: Boolean = false,
) {
    companion object {
        const val tableName = "active_so"
    }
}
