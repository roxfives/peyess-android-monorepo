package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toFSDenormalizedClient
import com.peyess.salesapp.data.adapter.payment.toFSPayment
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toFSPurchaseProductsDiscount
import com.peyess.salesapp.data.adapter.service_order.toFSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PurchaseDocument.toFSPurchase(): FSPurchase {
    return FSPurchase(
        id = id,

        storeId = storeId,
        storeIds = storeIds,

        clientUids = clientUids,
        clients = clients.map { it.toFSDenormalizedClient() },

        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsiblePicture = responsiblePicture,
        responsibleUid = responsibleUid,

        hasWitness = hasWitness,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessPicture = witnessPicture,
        witnessUid = witnessUid,

        salespersonUid = salespersonUid,

        isDiscountPerProduct = isDiscountPerProduct,
        overallDiscount = overallDiscount.toFSDiscountDescription(),

        price = price,
        priceWithDiscount = priceWithDiscount,
        prodDiscount = prodDiscount.mapValues { it.value.toFSPurchaseProductsDiscount() },

        state = state.toName(),

        payerUids = payerUids,
        payerDocuments = payerUids,
        payments = payments.map { it.toFSPayment() },

        soStates = soStates.mapValues { it.value.toName() },
        soIds = soIds,
        soPreviews = soPreviews.mapValues { it.value.toFSDenormalizedServiceOrderDesc() },
        soWithIssues = soWithIssues,
        hasRectifiedSo = hasRectifiedSo,
        isLegalCustom = isLegalCustom,

        legalText = legalText,
        legalVersion = legalVersion,

        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
