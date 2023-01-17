package com.peyess.salesapp.features.disponibility

import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription

private fun checkLensTypeMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isTypeSupported = false

    disponibilities.forEach {
        isTypeSupported = (prescription.lensType.isLensTypeMono() && it.isLensTypeMono)
                || (!prescription.lensType.isLensTypeMono() && !it.isLensTypeMono)

        if (isTypeSupported) {
            return@forEach
        }
    }

    if (prescription.lensType.isLensTypeMono() && !isTypeSupported) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMono)
    } else if (!prescription.lensType.isLensTypeMono() && !isTypeSupported) {
        reasonsUnsupported.add(ReasonUnsupported.LensTypeShouldBeMulti)
    }

    return reasonsUnsupported
}

private fun checkSphericalMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    disponibilities.forEach {
        isLeftSupported = isLeftSupported
                || prescription.leftSpherical <= it.maxSpherical
        isLeftSupported = isLeftSupported
                || prescription.leftSpherical >= it.minSpherical

        isRightSupported = isRightSupported
                || prescription.rightSpherical <= it.maxSpherical
        isRightSupported = isRightSupported
                || prescription.rightSpherical >= it.minSpherical
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.SphericalLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.SphericalRight)
    }

    return reasonsUnsupported
}

private fun checkCylindricalMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false


    disponibilities.forEach {
        isLeftSupported = isLeftSupported
                || prescription.leftCylindrical <= it.maxCylindrical
        isLeftSupported = isLeftSupported
                || prescription.leftCylindrical >= it.minCylindrical

        isRightSupported = isRightSupported
                || prescription.rightCylindrical <= it.maxCylindrical
        isRightSupported = isRightSupported
                || prescription.rightCylindrical >= it.minCylindrical
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.CylindricalLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.CylindricalRight)
    }

    return reasonsUnsupported
}

private fun checkAdditionMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    if (!prescription.hasAddition) {
        isLeftSupported = true
        isRightSupported = true
    } else {
        disponibilities.forEach {
            isLeftSupported = isLeftSupported
                    || prescription.leftAddition <= it.maxAddition
            isLeftSupported = isLeftSupported
                    || prescription.leftAddition >= it.minAddition

            isRightSupported = isRightSupported
                    || prescription.rightAddition <= it.maxAddition
            isRightSupported = isRightSupported
                    || prescription.rightAddition >= it.minAddition
        }
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.AdditionLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.AdditionRight)
    }

    return reasonsUnsupported
}

private fun checkPrismMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    if (!prescription.hasPrism) {
        isLeftSupported = true
        isRightSupported = true
    } else {
        disponibilities.forEach {
            isLeftSupported = isLeftSupported
                    || (it.hasPrism && prescription.leftPrism <= it.prism)

            isRightSupported = isRightSupported
                    || (it.hasPrism && prescription.rightPrism <= it.prism)
        }
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.PrismLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.PrismRight)
    }

    return reasonsUnsupported
}

private fun checkDiameterMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    disponibilities.forEach {
        isLeftSupported = isLeftSupported
                || prescription.leftDiameter <= it.diameter

        isRightSupported = isRightSupported
                || prescription.rightDiameter <= it.diameter
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.DiameterLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.DiameterRight)
    }

    return reasonsUnsupported
}

private fun checkHeightMatch(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    disponibilities.forEach {
        isLeftSupported = isLeftSupported
                || prescription.leftHeight >= it.height

        isRightSupported = isRightSupported
                || prescription.rightHeight >= it.height
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.HeightLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.HeightRight)
    }

    return reasonsUnsupported
}

//private fun checkAlternativeHeights(
//    disponibilities: List<Disponibility>,
//    prescription: Prescription,
//): List<ReasonUnsupported> {
//    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()
//
//    var isLeftSupported = false
//    var isRightSupported = false
//
//    disponibilities.forEach { disponibility ->
//        if (disponibility.hasAlternativeHeight) {
//            disponibility.alternativeHeights.forEach {
//                isLeftSupported = isLeftSupported
//                        || prescription.leftHeight >= it.value
//
//                isRightSupported = isRightSupported
//                        || prescription.rightHeight >= it.value
//            }
//        }
//    }
//
//    if (!isLeftSupported) {
//        reasonsUnsupported.add(ReasonUnsupported.HeightLeft)
//    }
//
//    if (!isRightSupported) {
//        reasonsUnsupported.add(ReasonUnsupported.HeightRight)
//    }
//
//    return reasonsUnsupported
//}

private fun checkSumRule(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    var isLeftSupported = false
    var isRightSupported = false

    disponibilities.forEach {
        if (!it.sumRule) {
            isLeftSupported = true
            isRightSupported = true

            return@forEach
        }

        isLeftSupported = isLeftSupported
                || prescription.leftSpherical + prescription.leftCylindrical >= it.minSpherical

        isRightSupported = isRightSupported
                || prescription.rightSpherical + prescription.rightCylindrical >= it.minSpherical
    }

    if (!isLeftSupported) {
        reasonsUnsupported.add(ReasonUnsupported.SumRuleLeft)
    }

    if (!isRightSupported) {
        reasonsUnsupported.add(ReasonUnsupported.SumRuleRight)
    }

    return reasonsUnsupported
}

fun supportsPrescription(
    disponibilities: List<Disponibility>,
    prescription: Prescription,
): List<ReasonUnsupported> {
    val reasonsUnsupported = mutableListOf<ReasonUnsupported>()

    reasonsUnsupported.addAll(checkLensTypeMatch(disponibilities, prescription))
    reasonsUnsupported.addAll(checkSphericalMatch(disponibilities, prescription))
    reasonsUnsupported.addAll(checkCylindricalMatch(disponibilities, prescription))
    reasonsUnsupported.addAll(checkDiameterMatch(disponibilities, prescription))


    reasonsUnsupported.addAll(checkAdditionMatch(disponibilities, prescription))

    reasonsUnsupported.addAll(checkPrismMatch(disponibilities, prescription))

    reasonsUnsupported.addAll(checkHeightMatch(disponibilities, prescription))

    reasonsUnsupported.addAll(checkSumRule(disponibilities, prescription))

    return reasonsUnsupported
}
