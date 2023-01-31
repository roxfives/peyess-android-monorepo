package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldDescriptionDocument
import com.peyess.salesapp.data.adapter.product_sold_desc.toProductSoldFramesDescriptionDocument
import com.peyess.salesapp.data.adapter.products_sold.toProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.utils.time.toZonedDateTime


fun FSServiceOrder.toServiceOrderDocument(): ServiceOrderDocument {
    return ServiceOrderDocument(
        id = id,
        hid = hid,

        storeId = storeId,
        storeIds = storeIds,

        clientUid = clientUid,
        clientDocument = clientDocument,
        clientName = clientName,
        clientBirthday = clientBirthday.toZonedDateTime(),
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
        responsibleBirthday = responsibleBirthday.toZonedDateTime(),
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
        witnessBirthday = witnessBirthday.toZonedDateTime(),
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
        leftProducts = leftProducts.toProductSoldEyeSetDocument(),
        rightProducts = rightProducts.toProductSoldEyeSetDocument(),
        framesProducts = framesProducts.toProductSoldFramesDescriptionDocument(),
        miscProducts = miscProducts.map { it.toProductSoldDescriptionDocument() },

        state = state,
        rectified = rectified,

        areThereIssues = areThereIssues,
        issueDescription = issueDescription,

        purchaseId = purchaseId,
        samePurchaseSo = samePurchaseSo,

        payerUids = payerUids,
        payerDocuments = payerDocuments,

        prescriptionId = prescriptionId,

        isCopy = isCopy,
        professionalName = professionalName,
        professionalId = professionalId,
        prescriptionDate = prescriptionDate.toZonedDateTime(),

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

        lPositioningId = lPositioningId,
        rPositioningId = rPositioningId,

        lIpd = lIpd,
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

        salespersonUid = salespersonUid,
        soldBy = soldBy,

        measureConfirmedBy = measureConfirmedBy,
        discountAllowedBy = discountAllowedBy,

        leftToPay = leftToPay,
        total = total,
        totalPaid = totalPaid,
        totalDiscount = totalDiscount,
        totalFee = totalFee,

        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
