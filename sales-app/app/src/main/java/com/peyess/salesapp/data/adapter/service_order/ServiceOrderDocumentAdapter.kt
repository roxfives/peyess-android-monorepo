package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toFSProductSoldFramesDescription
import com.peyess.salesapp.data.adapter.product_sold_desc.toFSSoldProductDescription
import com.peyess.salesapp.data.adapter.products_sold.toFSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun ServiceOrderDocument.toFSServiceOrder(): FSServiceOrder {
    return FSServiceOrder(
        id = id,
        hid = hid,
        storeId = storeId,
        storeIds = storeIds,
        picture = picture,
        salespersonUid = salespersonUid,

        clientUid = clientUid,
        clientDocument = clientDocument,
        clientName = clientName,
        clientPicture = clientPicture,
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
        responsiblePicture = responsiblePicture,
        responsibleBirthday = responsibleBirthday.toTimestamp(),
        responsiblePhone = responsiblePhone,
        responsibleCellphone = responsibleCellphone,
        responsibleNeighborhood = responsibleNeighborhood,
        responsibleStreet = responsibleStreet,
        responsibleCity = responsibleCity,
        responsibleState = responsibleState,
        responsibleHouseNumber = responsibleHouseNumber,
        responsibleZipcode = responsibleZipcode,

        hasWitness = hasWitness,
        witnessUid = witnessUid,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessPicture = witnessPicture,
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

        hasOwnFrames = hasOwnFrames,
        leftProducts = leftProducts.toFSProductSoldEyeSet(),
        rightProducts = rightProducts.toFSProductSoldEyeSet(),
        framesProducts = framesProducts.toFSProductSoldFramesDescription(),
        miscProducts = miscProducts.map { it.toFSSoldProductDescription() },

        state = state,
        rectified = rectified,
        areThereIssues = areThereIssues,
        issueDescription = issueDescription,
        samePurchaseSo = samePurchaseSo,
        purchaseId = purchaseId,
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        prescriptionId = prescriptionId,

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
        lPositioningId = lPositioningId,
        rPositioningId = rPositioningId,
        lMeasuringId = lMeasuringId,
        rMeasuringId = rMeasuringId,
        lBridge = lBridge,
        lDiameter = lDiameter,
        lHe = lHe,
        lHorizontalBridgeHoop = lHorizontalBridgeHoop,
        lHorizontalHoop = lHorizontalHoop,
        lVerticalHoop = lVerticalHoop,
        rIpd = rIpd,
        rBridge = rBridge,
        rDiameter = rDiameter,
        rHe = rHe,
        rHorizontalBridgeHoop = rHorizontalBridgeHoop,
        rHorizontalHoop = rHorizontalHoop,
        rVerticalHoop = rVerticalHoop,
        soldBy = soldBy,
        measureConfirmedBy = measureConfirmedBy,
        discountAllowedBy = discountAllowedBy,

        leftToPay = leftToPay,
        total = total,
        totalPaid = totalPaid,
        totalDiscount = totalDiscount,
        totalFee = totalFee,

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}

fun ServiceOrderDocument.toPreview(): DenormalizedServiceOrderDescDocument {
    return DenormalizedServiceOrderDescDocument(
        hasOwnFrames = hasOwnFrames,

        leftProducts = leftProducts,
        rightProducts = rightProducts,

        framesProducts = framesProducts,

        miscProducts = miscProducts,
    )
}