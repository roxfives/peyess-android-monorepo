package com.peyess.salesapp.features.frames.prescription_compatibility.model

import com.peyess.salesapp.constants.maxAxis
import com.peyess.salesapp.constants.maxPrismAxis
import com.peyess.salesapp.constants.minAddition
import com.peyess.salesapp.constants.minAxis
import com.peyess.salesapp.constants.minPrismAxis
import com.peyess.salesapp.constants.minPrismDegree
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.utils.math.middle
import kotlin.math.floor

data class PrescriptionCompatibility(
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
