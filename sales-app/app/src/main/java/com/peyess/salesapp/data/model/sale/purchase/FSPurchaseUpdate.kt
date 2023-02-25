package com.peyess.salesapp.data.model.sale.purchase

import com.google.firebase.Timestamp
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.fee.FSFeeDescription

data class FSPurchaseUpdate(
    val clientUids: List<String> = emptyList(),
    val clients: List<FSDenormalizedClient> = emptyList(),

    val responsibleUid: String = "",
    val responsibleDocument: String = "",
    val responsibleName: String = "",
    val responsibleBirthday: Timestamp = Timestamp.now(),
    val responsiblePhone: String = "",
    val responsibleCellphone: String = "",
    val responsibleNeighborhood: String = "",
    val responsibleStreet: String = "",
    val responsibleCity: String = "",
    val responsibleState: String = "",
    val responsibleHouseNumber: String = "",
    val responsibleZipcode: String = "",

    val hasWitness: Boolean = false,
    val witnessUid: String = "",
    val witnessDocument: String = "",
    val witnessName: String = "",
    val witnessBirthday: Timestamp = Timestamp.now(),
    val witnessPhone: String = "",
    val witnessCellphone: String = "",
    val witnessNeighborhood: String = "",
    val witnessStreet: String = "",
    val witnessCity: String = "",
    val witnessState: String = "",
    val witnessHouseNumber: String = "",
    val witnessZipcode: String = "",

    val isDiscountOverall: Boolean = true,
    val overallDiscount: FSDiscountDescription = FSDiscountDescription(),

    val paymentFee: FSFeeDescription = FSFeeDescription(),
    val discountServiceOrder: Map<String, FSPurchaseProductsDiscount> = emptyMap(),

    val fullPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val leftToPay: Double = 0.0,
    val totalPaid: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val totalFee: Double = 0.0,

    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),
    val payments: List<FSPayment> = emptyList(),

    val soIds: List<String> = emptyList(),
    val soPreviews: Map<String, FSDenormalizedServiceOrderDesc> = emptyMap(),

    val legalText: String = "",
    val legalVersion: String = "",

    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
