package com.peyess.salesapp.features.disponibility

import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription

private fun checkLensTypeMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (
        prescription.lensType.isLensTypeMono() && !disponibility.isLensTypeMono
    ) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMono)
    } else if (
        !prescription.lensType.isLensTypeMono() && disponibility.isLensTypeMono
    ) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMulti)
    }

    return reasonsUnsupported
}

private fun checkSphericalMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftSpherical > disponibility.maxSpherical) {
        reasonsUnsupported.add(ReasonUnsupported.MaxSphericalLeft)
    }

    if (prescription.rightSpherical > disponibility.maxSpherical) {
        reasonsUnsupported.add(ReasonUnsupported.MaxSphericalRight)
    }

    if (prescription.leftSpherical < disponibility.minSpherical) {
        reasonsUnsupported.add(ReasonUnsupported.MinSphericalLeft)
    }

    if (prescription.rightSpherical < disponibility.minSpherical) {
        reasonsUnsupported.add(ReasonUnsupported.MinSphericalRight)
    }

    return reasonsUnsupported
}

private fun checkCylindricalMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftCylindrical > disponibility.maxCylindrical) {
        reasonsUnsupported.add(ReasonUnsupported.MaxCylindricalLeft)
    }

    if (prescription.rightCylindrical > disponibility.maxCylindrical) {
        reasonsUnsupported.add(ReasonUnsupported.MaxCylindricalRight)
    }

    if (prescription.leftCylindrical < disponibility.minCylindrical) {
        reasonsUnsupported.add(ReasonUnsupported.MinCylindricalLeft)
    }

    if (prescription.rightCylindrical < disponibility.minCylindrical) {
        reasonsUnsupported.add(ReasonUnsupported.MinCylindricalRight)
    }

    return reasonsUnsupported
}

private fun checkAdditionMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftAddition > disponibility.maxAddition) {
        reasonsUnsupported.add(ReasonUnsupported.MaxAdditionLeft)
    }

    if (prescription.rightAddition > disponibility.maxAddition) {
        reasonsUnsupported.add(ReasonUnsupported.MaxAdditionRight)
    }

    if (prescription.leftAddition < disponibility.minAddition) {
        reasonsUnsupported.add(ReasonUnsupported.MinAdditionLeft)
    }

    if (prescription.rightAddition < disponibility.minAddition) {
        reasonsUnsupported.add(ReasonUnsupported.MinAdditionRight)
    }

    return reasonsUnsupported
}

private fun checkPrismMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftPrism > disponibility.prism) {
        reasonsUnsupported.add(ReasonUnsupported.PrismLeft)
    }

    if (prescription.rightPrism > disponibility.prism) {
        reasonsUnsupported.add(ReasonUnsupported.PrismRight)
    }

    return reasonsUnsupported
}

private fun checkDiameterMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftDiameter > disponibility.diameter) {
        reasonsUnsupported.add(ReasonUnsupported.DiameterLeft)
    }

    if (prescription.rightDiameter > disponibility.diameter) {
        reasonsUnsupported.add(ReasonUnsupported.DiameterRight)
    }

    return reasonsUnsupported
}

private fun checkHeightMatch(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    if (prescription.leftHeight > disponibility.height) {
        reasonsUnsupported.add(ReasonUnsupported.HeightLeft)
    }

    if (prescription.rightHeight > disponibility.height) {
        reasonsUnsupported.add(ReasonUnsupported.HeightRight)
    }

    return reasonsUnsupported
}

private fun checkAlternativeHeights(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var supportsLeft = false
    var supportsRight = false

    disponibility.alternativeHeights.forEach { alternativeHeight ->
        if (prescription.leftHeight <= alternativeHeight.value) {
            supportsLeft = true
        }

        if (prescription.rightHeight <= alternativeHeight.value) {
            supportsRight = true
        }
    }

    if (!supportsLeft) {
        reasonsUnsupported.add(ReasonUnsupported.HeightLeft)
    }

    if (!supportsRight) {
        reasonsUnsupported.add(ReasonUnsupported.HeightRight)
    }

    return reasonsUnsupported
}

private fun checkSumRule(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    val leftSumRuleChecks =
        prescription.leftSpherical + prescription.leftCylindrical - disponibility.maxCylindrical >=
                disponibility.minSpherical
    val rightSumRuleChecks =
        prescription.rightSpherical + prescription.rightCylindrical - disponibility.maxCylindrical >=
                disponibility.minSpherical

    if (!leftSumRuleChecks) {
        reasonsUnsupported.add(ReasonUnsupported.SumRuleLeft)
    }

    if (!rightSumRuleChecks) {
        reasonsUnsupported.add(ReasonUnsupported.SumRuleRight)
    }

    return reasonsUnsupported
}

fun supportsPrescription(
    disponibility: Disponibility,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    reasonsUnsupported.addAll(checkLensTypeMatch(disponibility, prescription))
    reasonsUnsupported.addAll(checkSphericalMatch(disponibility, prescription))
    reasonsUnsupported.addAll(checkCylindricalMatch(disponibility, prescription))
    reasonsUnsupported.addAll(checkDiameterMatch(disponibility, prescription))

    if (prescription.hasAddition) {
        reasonsUnsupported.addAll(checkAdditionMatch(disponibility, prescription))
    }

    if (prescription.hasPrism) {
        reasonsUnsupported.addAll(checkPrismMatch(disponibility, prescription))
    }

    if (disponibility.hasAlternativeHeight) {
        reasonsUnsupported.addAll(checkAlternativeHeights(disponibility, prescription))
    } else {
        reasonsUnsupported.addAll(checkHeightMatch(disponibility, prescription))
    }

    if (disponibility.sumRule) {
        reasonsUnsupported.addAll(checkSumRule(disponibility, prescription))
    }

    return reasonsUnsupported
}
