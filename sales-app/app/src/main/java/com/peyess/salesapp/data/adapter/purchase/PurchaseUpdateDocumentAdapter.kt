package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toFSDenormalizedClient
import com.peyess.salesapp.data.adapter.payment.toFSPayment
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSPurchaseProductsDiscount
import com.peyess.salesapp.data.adapter.purchase.fee.toFSFeeDescription
import com.peyess.salesapp.data.adapter.service_order.toFSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate
import com.peyess.salesapp.data.model.sale.purchase.PurchaseUpdateDocument
import com.peyess.salesapp.utils.extentions.roundToDouble
import com.peyess.salesapp.utils.time.toTimestamp

fun PurchaseUpdateDocument.toFSPurchaseUpdate(
    roundValues: Boolean = true,
): FSPurchaseUpdate {
    return FSPurchaseUpdate(
        clientUids = clientUids,
        clients = clients.mapValues { it.value.toFSDenormalizedClient() },
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
        fullPrice = fullPrice.roundToDouble(roundValues),
        finalPrice = finalPrice.roundToDouble(roundValues),
        leftToPay = leftToPay.roundToDouble(roundValues),
        totalPaid = totalPaid.roundToDouble(roundValues),
        totalDiscount = totalDiscount.roundToDouble(roundValues),
        totalFee = totalFee.roundToDouble(roundValues),
        payerUids = payerUids,
        payerDocuments = payerDocuments,
        payments = payments.map { it.toFSPayment() },
        soPreviews = soPreviews.mapValues { it.value.toFSDenormalizedServiceOrderDesc() },
        isLegalCustom = isLegalCustom,
        legalText = legalText,
        legalVersion = legalVersion,

        state = state.toName(),

        syncState = syncState.toName(),
        reasonSyncFailed = reasonSyncFailed.toName(),

        finishedAt = finishedAt.toTimestamp(),
        daysToTakeFromStore = daysToTakeFromStore,
        hasProductWithPendingCheck = hasProductWithPendingCheck,

        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}