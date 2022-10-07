package com.peyess.salesapp.data.adapter.service_order

import com.peyess.salesapp.data.adapter.product_sold_desc.toFSDenormalizedPurchaseDescription
import com.peyess.salesapp.data.adapter.products_sold.toProductsSoldDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.utils.time.toZonedDateTime


fun FSServiceOrder.toServiceOrderDocument(): ServiceOrderDocument {
    return ServiceOrderDocument(
        id = id,
        hid = hid,

        storeId = storeId,
        storeIds = storeIds,

        picture = picture,

        clientUid = clientUid,
        clientDocument = clientDocument,
        clientName = clientName,
        clientPicture = clientPicture,

        responsibleUid = responsibleUid,
        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsiblePicture = responsiblePicture,

        hasWitness = hasWitness,
        witnessUid = witnessUid,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessPicture = witnessPicture,

        state = state,
        rectified = rectified,

        areThereIssues = areThereIssues,
        issueDescription = issueDescription,

        purchaseId = purchaseId,
        samePurchaseSo = samePurchaseSo,

        payerUids = payerUids,
        payerDocuments = payerDocuments,
        total = total,
        products = products.toProductsSoldDocument(),

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

        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
