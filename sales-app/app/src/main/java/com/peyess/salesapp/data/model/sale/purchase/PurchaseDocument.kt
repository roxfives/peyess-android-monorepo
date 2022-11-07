package com.peyess.salesapp.data.model.sale.purchase

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.SOState
import java.time.ZonedDateTime

@Keep
@IgnoreExtraProperties
data class PurchaseDocument(
    val id: String = "",

    val storeId: String = "",
    val storeIds: List<String> = emptyList(),

    val clientUids: List<String> = emptyList(),
    val clients: List<DenormalizedClientDocument> = emptyList(),

    val responsibleUid: String = "",
    val responsibleDocument: String = "",
    val responsibleName: String = "",
    val responsiblePicture: String = "",
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
    val witnessPicture: String = "",
    val witnessBirthday: ZonedDateTime = ZonedDateTime.now(),
    val witnessPhone: String = "",
    val witnessCellphone: String = "",
    val witnessNeighborhood: String = "",
    val witnessStreet: String = "",
    val witnessCity: String = "",
    val witnessState: String = "",
    val witnessHouseNumber: String = "",
    val witnessZipcode: String = "",

    val salespersonUid: String = "",
    val salespersonName: String = "",

    val isDiscountPerProduct: Boolean = false,
    val overallDiscount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val price: Double = 0.0,
    val priceWithDiscount: Double = 0.0,
    val prodDiscount: Map<String, PurchaseProductsDiscountDocument> = emptyMap(),

    val state: PurchaseState = PurchaseState.Unknown,

    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),
    val payments: List<PaymentDocument> = emptyList(),

    val soStates: Map<String, SOState> = emptyMap(),
    val soIds: List<String> = emptyList(),
    val soPreviews: Map<String, DenormalizedServiceOrderDescDocument> = emptyMap(),
    val soWithIssues: List<String> = emptyList(),
    val hasRectifiedSo: Boolean = false,

    val isLegalCustom: Boolean = false,
    val legalText: String = "",
    val legalVersion: String = "",

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
