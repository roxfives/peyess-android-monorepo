package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toFSDenormalizedClient
import com.peyess.salesapp.data.adapter.payment.toFSPayment
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSPurchaseProductsDiscount
import com.peyess.salesapp.data.adapter.purchase.fee.toFSFeeDescription
import com.peyess.salesapp.data.adapter.service_order.toFSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PurchaseDocument.toFSPurchase(): FSPurchase {
    return FSPurchase(
        id = id,
        hid = hid,

        storeIds = storeIds,
        storeId = storeId,

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

        salespersonUid = salespersonUid,
        salespersonName = salespersonName,

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

        state = state.toName(),

        syncState = syncState.toName(),
        reasonSyncFailed = reasonSyncFailed.toName(),

        payerUids = payerUids,
        payerDocuments = payerDocuments,
        payments = payments.map { it.toFSPayment() },

        soStates = soStates.mapValues { it.value.toName() },
        soIds = soIds,
        soPreviews = soPreviews.mapValues { it.value.toFSDenormalizedServiceOrderDesc() },
        soWithIssues = soWithIssues,
        hasRectifiedSo = hasRectifiedSo,

        isLegalCustom = isLegalCustom,
        legalText = legalText,
        legalVersion = legalVersion,

        finishedAt = finishedAt.toTimestamp(),
        daysToTakeFromStore = daysToTakeFromStore,
        hasProductWithPendingCheck = hasProductWithPendingCheck,

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
