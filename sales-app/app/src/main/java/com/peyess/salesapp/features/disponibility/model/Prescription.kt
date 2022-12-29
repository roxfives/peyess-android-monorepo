package com.peyess.salesapp.features.disponibility.model

import com.peyess.salesapp.features.disponibility.contants.LensType

data class Prescription(
    val lensType: LensType = LensType.None,

    val leftDiameter: Double = 0.0,
    val rightDiameter: Double = 0.0,

    val leftHeight: Double = 0.0,
    val rightHeight: Double = 0.0,

    val leftCylindrical: Double = 0.0,
    val leftSpherical: Double = 0.0,
    val leftAddition: Double = 0.0,

    val rightCylindrical: Double = 0.0,
    val rightSpherical: Double = 0.0,
    val rightAddition: Double = 0.0,

    val hasPrism: Boolean = false,
    val leftPrism: Double = 0.0,
    val rightPrism: Double = 0.0,

    val hasAddition: Boolean = false,
    val sumRule: Boolean = false,
)
