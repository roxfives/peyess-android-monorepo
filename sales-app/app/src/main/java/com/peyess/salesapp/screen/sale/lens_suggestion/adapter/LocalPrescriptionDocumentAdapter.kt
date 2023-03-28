package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.feature.lens_suggestion.model.Measuring
import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.features.disponibility.model.Prescription

fun buildPrescription(
    localPrescriptionDocument: LocalPrescriptionDocument,
    localMeasuringLeft: LocalMeasuringDocument,
    localMeasuringRight: LocalMeasuringDocument,
): Prescription {
    return Prescription(
        lensTypeCategory = localPrescriptionDocument.lensTypeCategory,

        leftDiameter = localMeasuringLeft.fixedDiameter,
        rightDiameter = localMeasuringRight.fixedDiameter,
        leftHeight = localMeasuringLeft.fixedHe,
        rightHeight = localMeasuringRight.fixedHe,

        leftCylindrical = localPrescriptionDocument.cylindricalLeft,
        leftSpherical = localPrescriptionDocument.sphericalLeft,
        leftAddition = localPrescriptionDocument.additionLeft,
        rightCylindrical = localPrescriptionDocument.cylindricalRight,
        rightSpherical = localPrescriptionDocument.sphericalRight,
        rightAddition = localPrescriptionDocument.additionRight,
        hasPrism = localPrescriptionDocument.hasPrism,
        leftPrism = localPrescriptionDocument.prismDegreeLeft,
        rightPrism = localPrescriptionDocument.prismDegreeRight,
        hasAddition = localPrescriptionDocument.hasAddition,
    )
}


fun buildPrescription(
    localPrescriptionDocument: LocalPrescriptionDocument,
    localMeasuringLeft: Measuring,
    localMeasuringRight: Measuring,
): Prescription {
    return Prescription(
        lensTypeCategory = localPrescriptionDocument.lensTypeCategory,

        leftDiameter = localMeasuringLeft.fixedDiameter,
        rightDiameter = localMeasuringRight.fixedDiameter,
        leftHeight = localMeasuringLeft.fixedHe,
        rightHeight = localMeasuringRight.fixedHe,

        leftCylindrical = localPrescriptionDocument.cylindricalLeft,
        leftSpherical = localPrescriptionDocument.sphericalLeft,
        leftAddition = localPrescriptionDocument.additionLeft,
        rightCylindrical = localPrescriptionDocument.cylindricalRight,
        rightSpherical = localPrescriptionDocument.sphericalRight,
        rightAddition = localPrescriptionDocument.additionRight,
        hasPrism = localPrescriptionDocument.hasPrism,
        leftPrism = localPrescriptionDocument.prismDegreeLeft,
        rightPrism = localPrescriptionDocument.prismDegreeRight,
        hasAddition = localPrescriptionDocument.hasAddition,
    )
}
