package com.peyess.salesapp.feature.service_order.model

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

data class Prescription(
    val id: String = "",

    val soId: String = "",

    val pictureUri: Uri = Uri.EMPTY,
    val professionalName: String = "",
    val professionalId: String = "",
    val isCopy: Boolean = false,
    val prescriptionDate: ZonedDateTime = ZonedDateTime.now(),

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
)
