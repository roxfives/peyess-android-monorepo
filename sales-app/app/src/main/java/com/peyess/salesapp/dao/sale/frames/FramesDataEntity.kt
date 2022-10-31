package com.peyess.salesapp.dao.sale.frames

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import timber.log.Timber

sealed class FramesType {
    object MetalNylon: FramesType()
    object MetalEnclosed: FramesType()
    object MetalScrewed: FramesType()
    object AcetateEnclosed: FramesType()
    object AcetateNylon: FramesType()
    object AcetateScrewed: FramesType()
    object None: FramesType()

    fun toName() = toName(this)

    companion object {
        val listOfPositions by lazy {
            listOf(
                MetalNylon,
                MetalEnclosed,
                MetalScrewed,
                AcetateEnclosed,
                AcetateNylon,
                AcetateScrewed,
            )
        }

        fun toName(position: FramesType?): String {
            Timber.i("Translating position $position")

            return when(position) {
                AcetateEnclosed -> "Acetato Fechado"
                AcetateNylon -> "Acetato Nylon"
                AcetateScrewed -> "Acetato Parafusado"
                MetalEnclosed -> "Metal Fechado"
                MetalNylon -> "Metal Nylon"
                MetalScrewed -> "Metal Parafusado"
                None, null -> "None"
            }
        }

        fun toFramesType(name: String): FramesType {
            Timber.i("Translating name $name")

            return when(name) {
                "Acetato Fechado" -> AcetateEnclosed
                "Acetato Nylon" -> AcetateNylon
                "Acetato Parafusado" -> AcetateScrewed
                "Metal Fechado" -> MetalEnclosed
                "Metal Nylon" -> MetalNylon
                "Metal Parafusado" -> MetalScrewed
                else -> None
            }
        }
    }
}

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
    @PrimaryKey @ColumnInfo(name = "so_id") val soId: String = "",

    @ColumnInfo(name = "is_new") val areFramesNew: Boolean = false,

    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "reference") val reference: String = "",
    @ColumnInfo(name = "value") val value: Double = 0.0,
    @ColumnInfo(name = "tag_code") val tagCode: String = "",
    @ColumnInfo(name = "type") val type: FramesType? = null,

    @ColumnInfo(name = "info") val framesInfo: String = "",
) {
    companion object {
        const val tableName = "frames_data"
    }
}

fun FramesEntity.name(): String {
    return "$description, $reference ($tagCode)"
}

fun FramesEntity.hasPotentialProblemsWith(prescriptionData: PrescriptionDataEntity): Boolean {
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
