package com.peyess.salesapp.feature.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.local_sale.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.features.disponibility.contants.LensType
import com.peyess.salesapp.features.disponibility.model.Prescription

fun buildPrescription(
    lensType: LensType,
    localPrescriptionDocument: LocalPrescriptionDocument,
    localMeasuringLeft: LocalMeasuringDocument,
    localMeasuringRight: LocalMeasuringDocument,
): Prescription {
    return Prescription(
        lensType = lensType,
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
