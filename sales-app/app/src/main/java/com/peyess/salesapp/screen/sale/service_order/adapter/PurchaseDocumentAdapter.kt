package com.peyess.salesapp.screen.sale.service_order.adapter

import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.features.pdf.service_order.model.Purchase

fun PurchaseDocument.toPurchase(): Purchase {
    return Purchase(
        hid = hid,
        clientUids = clientUids,
        clients = clients,
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
        hasWitness = hasWitness,
        witnessUid = witnessUid,
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
        isDiscountOverall = isDiscountOverall,
        overallDiscount = overallDiscount,
        paymentFee = paymentFee,
        discountServiceOrder = discountServiceOrder,
        fullPrice = fullPrice,
        finalPrice = finalPrice,
        leftToPay = leftToPay,
        totalPaid = totalPaid,
        totalDiscount = totalDiscount,
        totalFee = totalFee,
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        payments = payments,
        soIds = soIds,
        soPreviews = soPreviews,
        legalText = legalText,
        legalVersion = legalVersion,
        updated = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}