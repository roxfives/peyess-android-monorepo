package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.discount_description.FSDiscountDescription
import com.peyess.salesapp.typing.sale.PurchaseState

@Keep
@IgnoreExtraProperties
data class FSPurchase(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("store_ids")
    val storeIds: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("store_id")
    val storeId: String = "",

    @Keep
    @JvmField
    @PropertyName("client_uids")
    val clientUids: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("clients")
    val clients: List<FSDenormalizedClient>,

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
    @PropertyName("responsible_picture")
    val responsiblePicture: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_uid")
    val responsibleUid: String = "",

    @Keep
    @JvmField
    @PropertyName("has_witness")
    val hasWitness: Boolean = false,
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
    @PropertyName("witness_picture")
    val witnessPicture: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_uid")
    val witnessUid: String = "",

    @Keep
    @JvmField
    @PropertyName("salesperson_uid")
    val salespersonUid: String = "",

    @Keep
    @JvmField
    @PropertyName("is_discount_per_product")
    val isDiscountPerProduct: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("overall_discount")
    val overallDiscount: FSDiscountDescription,
    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("price_with_discount")
    val priceWithDiscount: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("prod_discount")
    val prodDiscount: Map<String, FSPurchaseProductsDiscount>,

    @Keep
    @JvmField
    @PropertyName("state")
    val state: String = PurchaseState.Unknown.toName(),

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
    @PropertyName("so_states")
    val soStates: Map<String, String> = emptyMap(), //SOState
    @Keep
    @JvmField
    @PropertyName("so_ids")
    val soIds: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("so_previews")
    val soPreviews: Map<String, FSDenormalizedServiceOrderDesc> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("so_with_issues")
    val soWithIssues: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("has_rectified_so")
    val hasRectifiedSo: Boolean = false,

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
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

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
