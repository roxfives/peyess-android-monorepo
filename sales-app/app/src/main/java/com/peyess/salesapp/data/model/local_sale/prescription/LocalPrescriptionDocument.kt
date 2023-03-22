package com.peyess.salesapp.data.model.local_sale.prescription

import android.net.Uri
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
import kotlin.math.max
import kotlin.math.min

data class LocalPrescriptionDocument(
    val id: String = "",

    val soId: String = "",

    val pictureUri: Uri = Uri.EMPTY,
    val professionalName: String = "",
    val professionalId: String = "",
    val isCopy: Boolean = false,
    val prescriptionDate: ZonedDateTime = ZonedDateTime.now(),

    val lensTypeCategoryId: String = "",
    val lensTypeCategory: LensTypeCategoryName = LensTypeCategoryName.None,

    val sphericalLeft: Double = 0.0,
    val sphericalRight: Double = 0.0,
    val cylindricalLeft: Double = 0.0,
    val cylindricalRight: Double = 0.0,
    val axisLeft: Double = floor(middle(maxAxis, minAxis)),
    val axisRight: Double = floor(middle(maxAxis, minAxis)),
    val hasAddition: Boolean = false,
    val additionLeft: Double = minAddition,
    val additionRight: Double = minAddition,
    val hasPrism: Boolean = false,
    val prismDegreeLeft: Double = minPrismDegree,
    val prismDegreeRight: Double = minPrismDegree,
    val prismAxisLeft: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    val prismAxisRight: Double = floor(middle(maxPrismAxis, minPrismAxis)),
    val prismPositionLeft: PrismPosition = PrismPosition.None,
    val prismPositionRight: PrismPosition = PrismPosition.None,
) {
    private val idealBaseThreshold = 6.0

    private val idealBaseLeft = if (cylindricalLeft != 0.0) {
        (18.0 + (2.0 * sphericalLeft) + cylindricalLeft) / 3.0
    } else {
        (sphericalLeft + 12.0) / 2.0
    }
    private val idealBaseRight = if (cylindricalRight != 0.0) {
        (18.0 + (2.0 * sphericalRight) + cylindricalRight) / 3.0
    } else {
        (sphericalRight + 12.0) / 2.0;
    }

    private val maxIdealBase = max(idealBaseLeft, idealBaseRight)
    private val minIdealBase = min(idealBaseLeft, idealBaseRight)

    val prevalentIdealBase = if (maxIdealBase > idealBaseThreshold) {
        maxIdealBase
    } else {
        minIdealBase
    }
}
