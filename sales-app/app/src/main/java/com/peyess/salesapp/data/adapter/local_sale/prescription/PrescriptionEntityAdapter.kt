package com.peyess.salesapp.data.adapter.local_sale.prescription

import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.model.local_sale.LocalPrescriptionDocument
import com.peyess.salesapp.utils.time.toZonedDateTime

fun toLocalPrescriptionDocument(
    prescriptionDataEntity: PrescriptionDataEntity,
    prescriptionPictureEntity: PrescriptionPictureEntity,
): LocalPrescriptionDocument {
    return LocalPrescriptionDocument(
        soId = prescriptionDataEntity.soId,
        professionalName = prescriptionPictureEntity.professionalName,
        professionalId = prescriptionPictureEntity.professionalId,
        isCopy = prescriptionPictureEntity.isCopy,
        prescriptionDate = prescriptionPictureEntity.prescriptionDate.toZonedDateTime(),
        pictureUri = prescriptionPictureEntity.pictureUri,

        sphericalLeft = prescriptionDataEntity.sphericalLeft,
        sphericalRight = prescriptionDataEntity.sphericalRight,
        cylindricalLeft = prescriptionDataEntity.cylindricalLeft,
        cylindricalRight = prescriptionDataEntity.cylindricalRight,
        axisLeft = prescriptionDataEntity.axisLeft,
        axisRight = prescriptionDataEntity.axisRight,
        hasAddition = prescriptionDataEntity.hasAddition,
        additionLeft = prescriptionDataEntity.additionLeft,
        additionRight = prescriptionDataEntity.additionRight,
        hasPrism = prescriptionDataEntity.hasPrism,
        prismDegreeLeft = prescriptionDataEntity.prismDegreeLeft,
        prismDegreeRight = prescriptionDataEntity.prismDegreeRight,
        prismAxisLeft = prescriptionDataEntity.prismAxisLeft,
        prismAxisRight = prescriptionDataEntity.prismAxisRight,
        prismPositionLeft = prescriptionDataEntity.prismPositionLeft,
        prismPositionRight = prescriptionDataEntity.prismPositionRight,
    )
}
