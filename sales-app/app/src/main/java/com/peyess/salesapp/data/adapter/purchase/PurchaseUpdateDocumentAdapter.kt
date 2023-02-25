package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toFSDenormalizedClient
import com.peyess.salesapp.data.adapter.payment.toFSPayment
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSPurchaseProductsDiscount
import com.peyess.salesapp.data.adapter.purchase.fee.toFSFeeDescription
import com.peyess.salesapp.data.adapter.service_order.toFSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate
import com.peyess.salesapp.data.model.sale.purchase.PurchaseUpdateDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PurchaseUpdateDocument.toFSPurchaseUpdate(): FSPurchaseUpdate {
    return FSPurchaseUpdate(
        clientUids = clientUids,
        clients = clients.map { it.toFSDenormalizedClient() },
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
        hasWitness = hasWitness,
        witnessUid = witnessUid,
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
        isDiscountOverall = isDiscountOverall,
        overallDiscount = overallDiscount.toFSDiscountDescription(),
        paymentFee = paymentFee.toFSFeeDescription(),
        discountServiceOrder = discountServiceOrder.mapValues {
            it.value.toFSPurchaseProductsDiscount()
        },
        fullPrice = fullPrice,
        finalPrice = finalPrice,
        leftToPay = leftToPay,
        totalPaid = totalPaid,
        totalDiscount = totalDiscount,
        totalFee = totalFee,
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        payments = payments.map { it.toFSPayment() },
        soIds = soIds,
        soPreviews = soPreviews.mapValues {
            it.value.toFSDenormalizedServiceOrderDesc()
        },
        legalText = legalText,
        legalVersion = legalVersion,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
