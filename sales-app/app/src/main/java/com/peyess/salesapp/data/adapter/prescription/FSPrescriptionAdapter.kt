package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.adapter.internal.utils.lensTypeName
import com.peyess.salesapp.data.adapter.internal.utils.prismPositionName
import com.peyess.salesapp.data.model.prescription.FSPrescription
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument

fun prescriptionFrom(
    id: String,

    clientName: String,
    clientDocument: String,
    salespersonUid: String,

    dataEntity: PrescriptionDataEntity,
    pictureEntity: PrescriptionPictureEntity,
): PrescriptionDocument {
    return PrescriptionDocument(
        id = id,

        picture = pictureEntity.pictureUri.toString(),
        isCopy = pictureEntity.isCopy,

        patientDocument = clientDocument,
        patientName = clientName,

        professionalDocument = pictureEntity.professionalId,
        professionalName = pictureEntity.professionalName,

        hasAddition = dataEntity.hasAddition,
        lAddition = dataEntity.additionLeft,
        rAddition = dataEntity.additionRight,

        lCylinder = dataEntity.cylindricalLeft,
        lSpherical = dataEntity.sphericalLeft,
        lAxisDegree = dataEntity.axisLeft,

        hasPrism = dataEntity.hasPrism,
        lPrismAxis = dataEntity.prismAxisLeft,
        lPrismDegree = dataEntity.prismDegreeLeft,
        lPrismPos = dataEntity.prismPositionLeft,

        rCylinder = dataEntity.cylindricalRight,
        rSpherical = dataEntity.sphericalRight,
        rAxisDegree = dataEntity.axisRight,
        rPrismAxis = dataEntity.prismAxisRight,
        rPrismDegree = dataEntity.prismDegreeRight,
        rPrismPos = dataEntity.prismPositionRight,

        createdBy = salespersonUid,
        createAllowedBy = salespersonUid,
        updatedBy = salespersonUid,
        updateAllowedBy = salespersonUid,
    )
}

