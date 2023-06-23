package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.fee.FeeDescriptionDocument
import com.peyess.salesapp.typing.sale.PurchaseReasonSyncFailure
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState
import java.time.ZonedDateTime

data class PurchaseUpdateDocument(
    val clientUids: List<String> = emptyList(),
    val clients: List<DenormalizedClientDocument> = emptyList(),

    val responsibleUid: String = "",
    val responsibleDocument: String = "",
    val responsibleName: String = "",
    val responsibleBirthday: ZonedDateTime = ZonedDateTime.now(),
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
    val witnessBirthday: ZonedDateTime = ZonedDateTime.now(),
    val witnessPhone: String = "",
    val witnessCellphone: String = "",
    val witnessNeighborhood: String = "",
    val witnessStreet: String = "",
    val witnessCity: String = "",
    val witnessState: String = "",
    val witnessHouseNumber: String = "",
    val witnessZipcode: String = "",

    val isDiscountOverall: Boolean = true,
    val overallDiscount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val paymentFee: FeeDescriptionDocument = FeeDescriptionDocument(),
    val discountServiceOrder: Map<String, PurchaseProductsDiscountDocument> = emptyMap(),
    val fullPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val leftToPay: Double = 0.0,
    val totalPaid: Double = 0.0,
    val totalDiscount: Double = 0.0,
    val totalFee: Double = 0.0,

    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),
    val payments: List<PaymentDocument> = emptyList(),

    val soPreviews: Map<String, DenormalizedServiceOrderDescDocument> = emptyMap(),

    val isLegalCustom: Boolean = false,
    val legalText: String = "",
    val legalVersion: String = "",

    val state: PurchaseState = PurchaseState.PendingConfirmation,

    val syncState: PurchaseSyncState = PurchaseSyncState.NotSynced,
    val reasonSyncFailed: PurchaseReasonSyncFailure = PurchaseReasonSyncFailure.None,

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
