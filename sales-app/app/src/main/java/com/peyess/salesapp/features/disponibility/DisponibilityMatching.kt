package com.peyess.salesapp.features.disponibility

import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription

private fun checkLensTypeMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<Disponibility> {
    return disponibilities.filter {
        prescription.lensType.isLensTypeMono() && it.isLensTypeMono
                || !prescription.lensType.isLensTypeMono() && !it.isLensTypeMono
    }
}

private fun checkHeightMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<Disponibility> {
    return disponibilities.filter {
        prescription.leftHeight >= it.height && prescription.rightHeight >= it.height
    }
}

private fun checkDiameterMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<Disponibility> {
    return disponibilities.filter {
        prescription.leftDiameter <= it.diameter && prescription.rightDiameter <= it.diameter
    }
}

private fun checkPrismMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val hasPrism = disponibilities.first().hasPrism
    val maxPrism = disponibilities.maxOf { it.prism }

    val isLeftSupported = prescription.leftPrism <= maxPrism
    val isRightSupported = prescription.rightPrism <= maxPrism

    if (!hasPrism || !isLeftSupported || !isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.Prism)
    }

    return reasonsUnsupported
}

private fun checkAdditionMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val maxAddition = disponibilities.maxOf { it.maxAddition }
    val minAddition = disponibilities.minOf { it.minAddition }

    val isLeftSupported = prescription.leftAddition in minAddition..maxAddition
    val isRightSupported = prescription.rightAddition in minAddition..maxAddition


    if (!isLeftSupported || !isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.Addition)
    }

    return reasonsUnsupported
}

private fun checkSphericalMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val maxSpherical = disponibilities.maxOf { it.maxSpherical }
    val minSpherical = disponibilities.minOf { it.minSpherical }

    val isLeftSupported = prescription.leftSpherical in minSpherical..maxSpherical
    val isRightSupported = prescription.rightSpherical in minSpherical..maxSpherical

    if (!isLeftSupported || !isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.Spherical)
    }

    return reasonsUnsupported
}

private fun checkCylindricalMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val maxCylindrical = disponibilities.maxOf { it.maxCylindrical }
    val minCylindrical = disponibilities.minOf { it.minCylindrical }

    val isLeftSupported = prescription.leftCylindrical in minCylindrical..maxCylindrical
    val isRightSupported = prescription.rightCylindrical in minCylindrical..maxCylindrical

    if (!isLeftSupported || !isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.Cylindrical)
    }

    return reasonsUnsupported
}

private fun checkSumRule(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<Disponibility> {
    val matchingSumRuleLeft = disponibilities.filter {
        !it.sumRule
                || prescription.leftSpherical + prescription.leftCylindrical >= it.minSpherical
    }
    val matchingSumRuleRight = disponibilities.filter {
        !it.sumRule
                || prescription.rightSpherical + prescription.rightCylindrical >= it.minSpherical
    }

    return matchingSumRuleLeft.toSet()
        .union(matchingSumRuleRight.toSet())
        .toList()
}

fun supportsPrescription(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val matchingTypes = checkLensTypeMatch(disponibilities, prescription)
    if (matchingTypes.isEmpty() && prescription.lensType.isLensTypeMono()) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMono)
        return reasonsUnsupported
    } else if (matchingTypes.isEmpty() && !prescription.lensType.isLensTypeMono()) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMulti)
        return reasonsUnsupported
    }

    val matchingHeights = checkHeightMatch(matchingTypes, prescription)
    if (matchingHeights.isEmpty()) {
        reasonsUnsupported.add(ReasonUnsupported.Height)
        return reasonsUnsupported
    }

    val matchingDiameter = checkDiameterMatch(matchingHeights, prescription)
    if (matchingDiameter.isEmpty()) {
        reasonsUnsupported.add(ReasonUnsupported.Diameter)
        return reasonsUnsupported
    }

    val matchingSumRule = checkSumRule(matchingDiameter, prescription)
    if (matchingSumRule.isEmpty()) {
        reasonsUnsupported.add(ReasonUnsupported.SumRule)
        return reasonsUnsupported
    }

    if (prescription.hasPrism) {
        reasonsUnsupported.addAll(checkPrismMatch(matchingDiameter, prescription))
    }

    if (prescription.hasAddition) {
        reasonsUnsupported.addAll(checkAdditionMatch(matchingDiameter, prescription))
    }

    reasonsUnsupported.addAll(checkSphericalMatch(matchingDiameter, prescription))
    reasonsUnsupported.addAll(checkCylindricalMatch(matchingDiameter, prescription))

    return reasonsUnsupported
}
