package com.peyess.salesapp.dao.sale.prescription_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.feature.sale.prescription_data.state.maxAxis
import com.peyess.salesapp.feature.sale.prescription_data.state.maxPrismAxis
import com.peyess.salesapp.feature.sale.prescription_data.state.minAddition
import com.peyess.salesapp.feature.sale.prescription_data.state.minAxis
import com.peyess.salesapp.feature.sale.prescription_data.state.minPrismAxis
import com.peyess.salesapp.feature.sale.prescription_data.state.minPrismDegree
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.max

sealed class PrismPosition {
    object None: PrismPosition()
    object Nasal: PrismPosition()
    object Temporal: PrismPosition()
    object Superior: PrismPosition()
    object Inferior: PrismPosition()
    object Axis: PrismPosition()

    fun toName() = toName(this)

    companion object {
        val listOfPositions: List<PrismPosition> by lazy {
            listOf(
                None,
                Nasal,
                Temporal,
                Superior,
                Inferior,
                Axis,
            )
        }

        fun toName(position: PrismPosition?): String {
            return when(position) {
                is Nasal -> "Nasal"
                is Temporal -> "Temporal"
                is Superior -> "Superior"
                is Inferior -> "Inferior"
                is Axis -> "Eixo"
                else -> "Nenhuma"
            }
        }

        fun toPrism(name: String?): PrismPosition {
            return when(name?.lowercase()) {
                "nasal" ->  Nasal
                "temporal" ->  Temporal
                "superior" ->  Superior
                "inferior" -> Inferior
                "eixo" -> Axis
                else -> None
            }
        }
    }
}

@Entity(
    tableName = PrescriptionDataEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class PrescriptionDataEntity(
    @PrimaryKey @ColumnInfo(name = "so_id") val soId: String = "",

    @ColumnInfo(name = "spherical_left") val sphericalLeft: Double = 0.0,
    @ColumnInfo(name = "spherical_right") val sphericalRight: Double = 0.0,
    @ColumnInfo(name = "cylindrical_left") val cylindricalLeft: Double = 0.0,
    @ColumnInfo(name = "cylindrical_right") val cylindricalRight: Double = 0.0,
    @ColumnInfo(name = "axis_left") val axisLeft: Double = floor(middle(maxAxis, minAxis)),
    @ColumnInfo(name = "axis_right") val axisRight: Double = floor(middle(maxAxis, minAxis)),

    @ColumnInfo(name = "has_addition") val hasAddition: Boolean = false,
    @ColumnInfo(name = "addition_left") val additionLeft: Double = minAddition,
    @ColumnInfo(name = "addition_right") val additionRight: Double = minAddition,

    @ColumnInfo(name = "has_prism") val hasPrism: Boolean = false,
    @ColumnInfo(name = "prism_degree_left") val prismDegreeLeft: Double = minPrismDegree,
    @ColumnInfo(name = "prism_degree_right") val prismDegreeRight: Double = minPrismDegree,
    @ColumnInfo(name = "prism_axis_left") val prismAxisLeft: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    @ColumnInfo(name = "prism_axis_right") val prismAxisRight: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    @ColumnInfo(name = "prism_position_left") val prismPositionLeft: PrismPosition = PrismPosition.None,
    @ColumnInfo(name = "prism_position_right") val prismPositionRight: PrismPosition = PrismPosition.None,
) {
    companion object {
        const val tableName = "prescription_data"
        const val idealBaseThreshold = 6.0
    }
}

fun PrescriptionDataEntity.idealBaseLeft(): Double {
    return if (cylindricalLeft != 0.0) {
        (18.0 + (2.0 * sphericalLeft) + cylindricalLeft) / 3.0
    } else {
        (sphericalLeft + 12.0) / 2.0
    }
}

fun PrescriptionDataEntity.idealBaseRight(): Double {
    return if (cylindricalRight != 0.0) {
        (18.0 + (2.0 * sphericalRight) + cylindricalRight) / 3.0
    } else {
        (sphericalRight + 12.0) / 2.0;
    }
}

fun PrescriptionDataEntity.prevalentIdealBase(): Double {
    val maxIdealBase = idealBaseLeft().coerceAtLeast(idealBaseRight())
    val minIdealBase = idealBaseLeft().coerceAtMost(idealBaseRight())

    return if (maxIdealBase > PrescriptionDataEntity.idealBaseThreshold) {
        maxIdealBase
    } else {
        minIdealBase
    }
}

private fun middle(a: Double, b: Double): Double {
    return max(a, b) - ((a - b).absoluteValue / 2.0)
}
