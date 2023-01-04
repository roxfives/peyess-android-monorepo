package com.peyess.salesapp.data.dao.local_sale.prescription_picture

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import java.time.LocalDate

@Entity(
    tableName = PrescriptionPictureEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class PrescriptionPictureEntity(
    @PrimaryKey @ColumnInfo(name = "so_id")
    val soId: String = "",
    @ColumnInfo(name = "picture_uri")
    val pictureUri: Uri = Uri.EMPTY,
    @ColumnInfo(name = "professional_name")
    val professionalName: String = "",
    @ColumnInfo(name = "professional_id")
    val professionalId: String = "",
    @ColumnInfo(name = "is_copy")
    val isCopy: Boolean = false,
    @ColumnInfo(name = "local_date")
    val prescriptionDate: LocalDate = LocalDate.now(),
) {
    companion object {
        const val tableName = "prescription_picture"
    }
}
