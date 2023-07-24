package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toDenormalizedClientDocument
import com.peyess.salesapp.data.adapter.payment.toPaymentDocument
import com.peyess.salesapp.data.adapter.purchase.discount.description.toDiscountDescriptionDocument
import com.peyess.salesapp.data.adapter.purchase.discount.description.toPurchaseProductsDiscountDocument
import com.peyess.salesapp.data.adapter.purchase.fee.toFeeDescriptionDocument
import com.peyess.salesapp.data.adapter.service_order.toDenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.typing.sale.PurchaseReasonSyncFailure
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState
import com.peyess.salesapp.typing.sale.SOState
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSPurchase.toPurchaseDocument(): PurchaseDocument {
    return PurchaseDocument(
        id = id,
        hid = hid,

        storeId = storeId,
        storeIds = storeIds,

        clientUids = clientUids,
        clients = clients.mapValues {
            it.value.toDenormalizedClientDocument()
        },

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

        salespersonUid = salespersonUid,
        salespersonName = salespersonName,

        isDiscountOverall = isDiscountOverall,
        overallDiscount = overallDiscount.toDiscountDescriptionDocument(),
        paymentFee = paymentFee.toFeeDescriptionDocument(),
        discountServiceOrder = discountServiceOrder.mapValues {
            it.value.toPurchaseProductsDiscountDocument()
        },

        fullPrice = fullPrice,
        finalPrice = finalPrice,
        leftToPay = leftToPay,
        totalPaid = totalPaid,

        totalDiscount = totalDiscount,
        totalFee = totalFee,

        state = PurchaseState.fromName(state),

        syncState = PurchaseSyncState.fromName(syncState),
        reasonSyncFailed = PurchaseReasonSyncFailure.fromName(reasonSyncFailed),

        payerUids = payerUids,
        payerDocuments = payerDocuments,
        payments = payments.map { it.toPaymentDocument() },
        soStates = soStates.mapValues { SOState.fromName(it.value) },

        soIds = soIds,
        soPreviews = soPreviews.mapValues { it.value.toDenormalizedServiceOrderDescDocument() },
        soWithIssues = soWithIssues,

        hasRectifiedSo = hasRectifiedSo,
        isLegalCustom = isLegalCustom,
        legalText = legalText,
        legalVersion = legalVersion,

        finishedAt = finishedAt.toZonedDateTime(),
        daysToTakeFromStore = daysToTakeFromStore,
        hasProductWithPendingCheck = hasProductWithPendingCheck,

        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
