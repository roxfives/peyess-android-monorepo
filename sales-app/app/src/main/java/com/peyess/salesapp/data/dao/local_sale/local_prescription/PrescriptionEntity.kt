package com.peyess.salesapp.data.dao.local_sale.local_prescription

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.constants.maxAxis
import com.peyess.salesapp.constants.maxPrismAxis
import com.peyess.salesapp.constants.minAddition
import com.peyess.salesapp.constants.minAxis
import com.peyess.salesapp.constants.minPrismAxis
import com.peyess.salesapp.constants.minPrismDegree
import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.utils.math.middle
import java.time.ZonedDateTime
import kotlin.math.floor

@Entity(
    tableName = PrescriptionEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class PrescriptionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "so_id")
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
    val prescriptionDate: ZonedDateTime = ZonedDateTime.now(),

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

    @ColumnInfo(name = "lens_type_category_id")
    val lensTypeCategoryId: String = "",
    @ColumnInfo(name = "lens_type_category")
    val lensTypeCategory: LensTypeCategoryName = LensTypeCategoryName.None,

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

    @ColumnInfo(name = "observation")
    val observation: String = "",
) {
    companion object {
        const val tableName = "local_prescription"
    }
}
