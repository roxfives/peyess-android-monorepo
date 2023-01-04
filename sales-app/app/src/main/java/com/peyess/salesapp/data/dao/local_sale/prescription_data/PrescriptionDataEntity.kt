package com.peyess.salesapp.data.dao.local_sale.prescription_data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.constants.maxAxis
import com.peyess.salesapp.constants.maxPrismAxis
import com.peyess.salesapp.constants.minAddition
import com.peyess.salesapp.constants.minAxis
import com.peyess.salesapp.constants.minPrismAxis
import com.peyess.salesapp.constants.minPrismDegree
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.utils.math.middle
import kotlin.math.floor

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
    @PrimaryKey @ColumnInfo(name = "so_id")
    val soId: String = "",

    @ColumnInfo(name = "spherical_left")
    val sphericalLeft: Double = 0.0,
    @ColumnInfo(name = "spherical_right")
    val sphericalRight: Double = 0.0,
    @ColumnInfo(name = "cylindrical_left")
    val cylindricalLeft: Double = 0.0,
    @ColumnInfo(name = "cylindrical_right")
    val cylindricalRight: Double = 0.0,
    @ColumnInfo(name = "axis_left")
    val axisLeft: Double = floor(middle(maxAxis, minAxis)),
    @ColumnInfo(name = "axis_right")
    val axisRight: Double = floor(middle(maxAxis, minAxis)),

    @ColumnInfo(name = "has_addition")
    val hasAddition: Boolean = false,
    @ColumnInfo(name = "addition_left")
    val additionLeft: Double = minAddition,
    @ColumnInfo(name = "addition_right")
    val additionRight: Double = minAddition,

    @ColumnInfo(name = "has_prism")
    val hasPrism: Boolean = false,
    @ColumnInfo(name = "prism_degree_left")
    val prismDegreeLeft: Double = minPrismDegree,
    @ColumnInfo(name = "prism_degree_right")
    val prismDegreeRight: Double = minPrismDegree,
    @ColumnInfo(name = "prism_axis_left")
    val prismAxisLeft: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    @ColumnInfo(name = "prism_axis_right")
    val prismAxisRight: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    @ColumnInfo(name = "prism_position_left")
    val prismPositionLeft: PrismPosition = PrismPosition.None,
    @ColumnInfo(name = "prism_position_right")
    val prismPositionRight: PrismPosition = PrismPosition.None,
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
