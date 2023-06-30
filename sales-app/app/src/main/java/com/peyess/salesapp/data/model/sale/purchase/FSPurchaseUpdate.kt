package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.fee.FSFeeDescription
import com.peyess.salesapp.typing.sale.PurchaseReasonSyncFailure
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState

data class FSPurchaseUpdate(
    @Keep
    @JvmField
    @PropertyName("client_uids")
    val clientUids: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("clients")
    val clients: List<FSDenormalizedClient> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("responsible_uid")
    val responsibleUid: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_document")
    val responsibleDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_name")
    val responsibleName: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_birthday")
    val responsibleBirthday: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("responsible_phone")
    val responsiblePhone: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_cellphone")
    val responsibleCellphone: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_neighborhood")
    val responsibleNeighborhood: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_street")
    val responsibleStreet: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_city")
    val responsibleCity: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_state")
    val responsibleState: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_housenumber")
    val responsibleHouseNumber: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_zipcode")
    val responsibleZipcode: String = "",

    @Keep
    @JvmField
    @PropertyName("has_witness")
    val hasWitness: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("witness_uid")
    val witnessUid: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_document")
    val witnessDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_name")
    val witnessName: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_birthday")
    val witnessBirthday: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("witness_phone")
    val witnessPhone: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_cellphone")
    val witnessCellphone: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_neighborhood")
    val witnessNeighborhood: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_street")
    val witnessStreet: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_city")
    val witnessCity: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_state")
    val witnessState: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_housenumber")
    val witnessHouseNumber: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_zipcode")
    val witnessZipcode: String = "",

    @Keep
    @JvmField
    @PropertyName("is_discount_overall")
    val isDiscountOverall: Boolean = true,
    @Keep
    @JvmField
    @PropertyName("overall_discount")
    val overallDiscount: FSDiscountDescription = FSDiscountDescription(),
    @Keep
    @JvmField
    @PropertyName("payment_fee")
    val paymentFee: FSFeeDescription = FSFeeDescription(),
    @Keep
    @JvmField
    @PropertyName("discount_so")
    val discountServiceOrder: Map<String, FSPurchaseProductsDiscount> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("full_price")
    val fullPrice: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("final_price")
    val finalPrice: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("left_to_pay")
    val leftToPay: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("total_paid")
    val totalPaid: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("total_discount")
    val totalDiscount: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("total_fee")
    val totalFee: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("payer_uids")
    val payerUids: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("payer_documents")
    val payerDocuments: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("payments")
    val payments: List<FSPayment> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("so_previews")
    val soPreviews: Map<String, FSDenormalizedServiceOrderDesc> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("is_legal_custom")
    val isLegalCustom: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("legal_text")
    val legalText: String = "",
    @Keep
    @JvmField
    @PropertyName("legal_version")
    val legalVersion: String = "",

    @Keep
    @JvmField
    @PropertyName("finished_at")
    val finishedAt: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("days_to_take_from_store")
    val daysToTakeFromStore: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("state")
    val state: String = PurchaseState.Unknown.toName(),

    @Keep
    @JvmField
    @PropertyName("sync_state")
    val syncState: String = PurchaseSyncState.Unknown.toName(),
    @Keep
    @JvmField
    @PropertyName("reason_sync_failed")
    val reasonSyncFailed: String = PurchaseReasonSyncFailure.None.toName(),

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",
    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)
