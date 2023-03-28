package com.peyess.salesapp.features.disponibility.utils

import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.features.disponibility.model.AlternativeHeight
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription

fun buildPrescription(
    lensType: LensType = LensType.MonofocalFar,
    hasAddition: Boolean = true,
    hasPrism: Boolean = true,

    leftSpherical: Double = 1.0,
    rightSpherical: Double = 1.5,
    leftCylindrical: Double = 1.0,
    rightCylindrical: Double = 0.0,
    leftAddition: Double = 1.0,
    rightAddition: Double = 0.0,

    leftDiameter: Double = 60.0,
    rightDiameter: Double = 60.0,

    leftHeight: Double = 10.0,
    rightHeight: Double = 10.0,

    leftPrism: Double = 2.0,
    rightPrism: Double = 2.0,
) = Prescription(
    lensType = lensType,
    leftDiameter = leftDiameter,
    rightDiameter = rightDiameter,
    leftHeight = leftHeight,
    rightHeight = rightHeight,
    leftCylindrical = leftCylindrical,
    leftSpherical = leftSpherical,
    leftAddition = leftAddition,
    rightCylindrical = rightCylindrical,
    rightSpherical = rightSpherical,
    rightAddition = rightAddition,
    hasPrism = hasPrism,
    leftPrism = leftPrism,
    rightPrism = rightPrism,
    hasAddition = hasAddition,
)

fun buildDisponibility(
    isLensTypeMono: Boolean = true,
    hasPrism: Boolean = true,
    sumRule: Boolean = true,

    maxSpherical: Double = 6.0,
    minSpherical: Double = -6.0,
    maxCylindrical: Double = 2.0,
    minCylindrical: Double = -2.0,

    maxAddition: Double = 2.0,
    minAddition: Double = 0.0,

    diameter: Double = 70.0,
    height: Double = 10.0,

    prism: Double = 2.0,

    hasAlternativeHeight: Boolean = false,
    alternativeHeights: List<AlternativeHeight> = emptyList(),
) =
Disponibility(
    diameter = diameter,
    height = height,
    isLensTypeMono = isLensTypeMono,
    maxCylindrical = maxCylindrical,
    minCylindrical = minCylindrical,
    maxSpherical = maxSpherical,
    minSpherical = minSpherical,
    maxAddition = maxAddition,
    minAddition = minAddition,
    hasPrism = hasPrism,
    prism = prism,
    sumRule = sumRule,
    hasAlternativeHeight = hasAlternativeHeight,
    alternativeHeights = alternativeHeights,
)
