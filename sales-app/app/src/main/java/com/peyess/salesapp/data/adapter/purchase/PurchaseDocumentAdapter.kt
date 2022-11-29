package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toFSDenormalizedClient
import com.peyess.salesapp.data.adapter.payment.toFSPayment
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSPurchaseProductsDiscount
import com.peyess.salesapp.data.adapter.service_order.toFSDenormalizedServiceOrderDesc
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.utils.time.toTimestamp

fun PurchaseDocument.toFSPurchase(): FSPurchase {
    return FSPurchase(
        id = id,
        hid = hid,

        storeId = storeId,
        storeIds = storeIds,

        clientUids = clientUids,
        clients = clients.map { it.toFSDenormalizedClient() },

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

        salespersonUid = salespersonUid,
        salespersonName = salespersonName,

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
