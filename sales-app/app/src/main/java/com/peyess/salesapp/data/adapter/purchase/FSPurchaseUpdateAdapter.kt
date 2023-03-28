package com.peyess.salesapp.data.adapter.purchase

import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate

fun FSPurchaseUpdate.toMappingUpdate(): Map<String, Any> {
    return mapOf(
        "client_uids" to clientUids,
        "clients" to clients,
        "responsible_uid" to responsibleUid,
        "responsible_document" to responsibleDocument,
        "responsible_name" to responsibleName,
        "responsible_birthday" to responsibleBirthday,
        "responsible_phone" to responsiblePhone,
        "responsible_cellphone" to responsibleCellphone,
        "responsible_neighborhood" to responsibleNeighborhood,
        "responsible_street" to responsibleStreet,
        "responsible_city" to responsibleCity,
        "responsible_state" to responsibleState,
        "responsible_housenumber" to responsibleHouseNumber,
        "responsible_zipcode" to responsibleZipcode,
        "has_witness" to hasWitness,
        "witness_uid" to witnessUid,
        "witness_document" to witnessDocument,
        "witness_name" to witnessName,
        "witness_birthday" to witnessBirthday,
        "witness_phone" to witnessPhone,
        "witness_cellphone" to witnessCellphone,
        "witness_neighborhood" to witnessNeighborhood,
        "witness_street" to witnessStreet,
        "witness_city" to witnessCity,
        "witness_state" to witnessState,
        "witness_housenumber" to witnessHouseNumber,
        "witness_zipcode" to witnessZipcode,
        "is_discount_overall" to isDiscountOverall,
        "overall_discount" to overallDiscount,
        "payment_fee" to paymentFee,
        "discount_so" to discountServiceOrder,
        "full_price" to fullPrice,
        "final_price" to finalPrice,
        "left_to_pay" to leftToPay,
        "total_paid" to totalPaid,
        "total_discount" to totalDiscount,
        "total_fee" to totalFee,
        "payer_uids" to payerUids,
        "payer_documents" to payerDocuments,
        "payments" to payments,
        "so_previews" to soPreviews,
        "is_legal_custom" to isLegalCustom,
        "legal_text" to legalText,
        "legal_version" to legalVersion,
        "updated" to updated,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
