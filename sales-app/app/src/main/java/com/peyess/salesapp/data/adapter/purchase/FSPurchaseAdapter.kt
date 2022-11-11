package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.adapter.client.toDenormalizedClientDocument
import com.peyess.salesapp.data.adapter.payment.toPaymentDocument
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toDiscountDescriptionDocument
import com.peyess.salesapp.data.adapter.purchase_discount_desc.toPurchaseProductsDiscountDocument
import com.peyess.salesapp.data.adapter.service_order.toDenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.SOState
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSPurchase.toPurchaseDocument(): PurchaseDocument {
    return PurchaseDocument(
        id = id,

        storeId = storeId,
        storeIds = storeIds,

        clientUids = clientUids,
        clients = clients.map { it.toDenormalizedClientDocument() },

        responsibleUid = responsibleUid,
        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsiblePicture = responsiblePicture,
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
        witnessPicture = witnessPicture,
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

        isDiscountPerProduct = isDiscountPerProduct,
        overallDiscount = overallDiscount.toDiscountDescriptionDocument(),

        price = price,
        priceWithDiscount = priceWithDiscount,
        prodDiscount = prodDiscount.mapValues { it.value.toPurchaseProductsDiscountDocument() },

        state = PurchaseState.fromName(state),

        payerUids = payerUids,
        payerDocuments = payerUids,
        payments = payments.map { it.toPaymentDocument() },

        soStates = soStates.mapValues { SOState.fromName(it.value) },
        soIds = soIds,
        soPreviews = soPreviews.mapValues {  it.value.toDenormalizedServiceOrderDescDocument() },
        soWithIssues = soWithIssues,
        hasRectifiedSo = hasRectifiedSo,
        isLegalCustom = isLegalCustom,

        legalText = legalText,
        legalVersion = legalVersion,

        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}
