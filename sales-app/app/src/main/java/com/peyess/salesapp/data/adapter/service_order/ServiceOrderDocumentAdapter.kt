package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toDenormalizedPurchaseDescription
import com.peyess.salesapp.data.adapter.products_sold.toFSProductsSold
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
        clientDocument = clientDocument,
        clientName = clientName,
        clientPicture = clientPicture,
        clientUid = clientUid,
        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsiblePicture = responsiblePicture,
        responsibleUid = responsibleUid,
        hasWitness = hasWitness,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessPicture = witnessPicture,
        witnessUid = witnessUid,
        state = state,
        rectified = rectified,
        areThereIssues = areThereIssues,
        issueDescription = issueDescription,
        samePurchaseSo = samePurchaseSo,
        purchaseId = purchaseId,
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        products = products.toFSProductsSold(),
        prescriptionId = prescriptionId,
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
        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
        total = total,
    )
}



fun ServiceOrderDocument.toPreview(): DenormalizedServiceOrderDescDocument {
    return DenormalizedServiceOrderDescDocument(
        lenses = products.lenses.map { (_, v) -> v.toDenormalizedPurchaseDescription() },
        colorings = products.colorings.map { (_, v) -> v.toDenormalizedPurchaseDescription() },
        treatments = products.treatments.map { (_, v) -> v.toDenormalizedPurchaseDescription() },

        frames = products.frames.toDenormalizedPurchaseDescription(),

        misc = products.colorings.map { (_, v) -> v.toDenormalizedPurchaseDescription() },
    )
}