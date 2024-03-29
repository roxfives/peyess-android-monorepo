package com.peyess.salesapp.features.edit_service_order.updater.adapter

import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderUpdateDocument
import com.peyess.salesapp.features.pdf.service_order.model.ServiceOrder
import java.time.ZonedDateTime

fun ServiceOrderUpdateDocument.toServiceOrder(
    hid: String,
    created: ZonedDateTime,
): ServiceOrder {
    return ServiceOrder(
        hid = hid,
        clientUid = clientUid,
        clientDocument = clientDocument,
        clientName = clientName,
        clientBirthday = clientBirthday,
        clientPhone = clientPhone,
        clientCellphone = clientCellphone,
        clientNeighborhood = clientNeighborhood,
        clientStreet = clientStreet,
        clientCity = clientCity,
        clientState = clientState,
        clientHouseNumber = clientHouseNumber,
        clientZipcode = clientZipcode,
        responsibleUid = responsibleUid,
        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsibleBirthday = responsibleBirthday,
        responsiblePhone = responsiblePhone,
        responsibleCellphone = responsibleCellphone,
        responsibleNeighborhood = responsibleNeighborhood,
        responsibleStreet = responsibleStreet,
        responsibleCity = responsibleCity,
        responsibleState = responsibleState,
        responsibleHouseNumber = responsibleHouseNumber,
        responsibleZipcode = responsibleZipcode,
        witnessUid = witnessUid,
        hasWitness = hasWitness,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessBirthday = witnessBirthday,
        witnessPhone = witnessPhone,
        witnessCellphone = witnessCellphone,
        witnessNeighborhood = witnessNeighborhood,
        witnessStreet = witnessStreet,
        witnessCity = witnessCity,
        witnessState = witnessState,
        witnessHouseNumber = witnessHouseNumber,
        witnessZipcode = witnessZipcode,
        hasTakeaway = hasTakeaway,
        takeawayName = takeawayName,
        takeawayDocument = takeawayDocument,
        samePurchaseSo = samePurchaseSo,
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        hasOwnFrames = hasOwnFrames,
        leftProducts = leftProducts,
        rightProducts = rightProducts,
        framesProducts = framesProducts,
        miscProducts = miscProducts,
        isCopy = isCopy,
        professionalName = professionalName,
        professionalId = professionalId,
        prescriptionDate = prescriptionDate,
        hasAddition = hasAddition,
        hasPrism = hasPrism,
        lCylinder = lCylinder,
        lSpheric = lSpheric,
        lAxisDegree = lAxisDegree,
        lAddition = lAddition,
        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = lPrismPos,
        rCylinder = rCylinder,
        rSpheric = rSpheric,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = rPrismPos,
        lIpd = lIpd,
        lBridge = lBridge,
        lBridgeHoop = lBridgeHoop,
        lDiameter = lDiameter,
        lHe = lHe,
        lHorizontalBridgeHoop = lHorizontalBridgeHoop,
        lHorizontalHoop = lHorizontalHoop,
        lVerticalHoop = lVerticalHoop,
        rIpd = rIpd,
        rBridge = rBridge,
        rBridgeHoop = rBridgeHoop,
        rDiameter = rDiameter,
        rHe = rHe,
        rHorizontalBridgeHoop = rHorizontalBridgeHoop,
        rHorizontalHoop = rHorizontalHoop,
        rVerticalHoop = rVerticalHoop,
        measureConfirmedBy = measureConfirmedBy,
        discountAllowedBy = discountAllowedBy,
        fullPrice = fullPrice,
        finalPrice = finalPrice,
        observation = observation,
        created = created,
        updated = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}

fun ServiceOrderUpdateDocument.toPreview(): DenormalizedServiceOrderDescDocument {
    return DenormalizedServiceOrderDescDocument(
        hasOwnFrames = hasOwnFrames,

        leftProducts = leftProducts,
        rightProducts = rightProducts,

        framesProducts = framesProducts,

        miscProducts = miscProducts,

        observation = observation,
        prescriptionId = prescriptionId,
    )
}