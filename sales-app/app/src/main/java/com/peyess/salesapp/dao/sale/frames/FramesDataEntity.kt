package com.peyess.salesapp.dao.sale.frames

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.typing.frames.FramesType

@Entity(
    tableName = FramesEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class FramesEntity(
    @PrimaryKey
    @ColumnInfo(name = "so_id")
    val soId: String = "",

    @ColumnInfo(name = "is_new")
    val areFramesNew: Boolean = false,

    @ColumnInfo(name = "description")
    val design: String = "",
    @ColumnInfo(name = "reference")
    val reference: String = "",
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
    @ColumnInfo(name = "tag_code")
    val tagCode: String = "",
    @ColumnInfo(name = "type")
    val type: FramesType = FramesType.None,

    @ColumnInfo(name = "info")
    val framesInfo: String = "",
) {
    companion object {
        const val tableName = "frames_data"
    }
}

fun FramesEntity.name(): String {
    return "$design, $reference ($tagCode)"
}

fun FramesEntity.hasPotentialProblemsWith(prescriptionData: LocalPrescriptionDocument): Boolean {
//    val hasProblemsOnLeft =
//        (prescriptionData.sphericalLeft <= -4.0) ||
//                ((prescriptionData.axisLeft <= 30.0 || prescriptionData.axisLeft == 180.0) &&
//                        prescriptionData.sphericalLeft + prescriptionData.cylindricalLeft <= -4.0)
//
//    val hasProblemsOnRight = (prescriptionData.sphericalRight <= -4.0) ||
//            ((prescriptionData.axisRight >= 150.0 || prescriptionData.axisRight == 0.0) &&
//                    prescriptionData.sphericalRight + prescriptionData.cylindricalRight <= -4.0)


    val hasProblemsOnLeft = prescriptionData.sphericalLeft <= -4.0
            || prescriptionData.sphericalLeft >= 4.0
            || prescriptionData.sphericalLeft + prescriptionData.cylindricalLeft <= -4.0

    val hasProblemsOnRight = prescriptionData.sphericalRight <= -4.0
            || prescriptionData.sphericalRight >= 4.0
            || prescriptionData.sphericalRight + prescriptionData.cylindricalRight <= -4.0

    return hasProblemsOnLeft || hasProblemsOnRight
}
