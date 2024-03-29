package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toFSProductSoldFramesDescription
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription
import com.peyess.salesapp.data.adapter.products_sold.toFSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrderUpdate
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderUpdateDocument
import com.peyess.salesapp.utils.extentions.roundToDouble
import com.peyess.salesapp.utils.time.toTimestamp

fun ServiceOrderUpdateDocument.toFSServiceOrderUpdate(
    roundValues: Boolean = false,
): FSServiceOrderUpdate {
    return FSServiceOrderUpdate(
        clientUid = clientUid,
        clientDocument = clientDocument,
        clientName = clientName,
        clientBirthday = clientBirthday.toTimestamp(),
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
        responsibleBirthday = responsibleBirthday.toTimestamp(),
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
        witnessBirthday = witnessBirthday.toTimestamp(),
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
        leftProducts = leftProducts.toFSProductSoldEyeSet(),
        rightProducts = rightProducts.toFSProductSoldEyeSet(),
        framesProducts = framesProducts.toFSProductSoldFramesDescription(),
        miscProducts = miscProducts.map { it.toFSSoldProductDescription() },
        isCopy = isCopy,
        professionalName = professionalName,
        professionalId = professionalId,
        prescriptionDate = prescriptionDate.toTimestamp(),
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
        fullPrice = fullPrice.roundToDouble(roundValues),
        finalPrice = finalPrice.roundToDouble(roundValues),
        observation = observation,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
