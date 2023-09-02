package com.peyess.salesapp.features.pdf.service_order.model

import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedServiceOrderDescDocument
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseProductsDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.fee.FeeDescriptionDocument
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZonedDateTime

data class Purchase(
    val hid: String = "",

    val clientUids: List<String> = emptyList(),
    val clients: Map<String, DenormalizedClientDocument> = emptyMap(),

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

    val fullPrice: BigDecimal = BigDecimal.ZERO,
    val finalPrice: BigDecimal = BigDecimal.ZERO,
    val leftToPay: BigDecimal = BigDecimal.ZERO,
    val totalPaid: BigDecimal = BigDecimal.ZERO,
    val totalDiscount: BigDecimal = BigDecimal.ZERO,
    val totalFee: BigDecimal = BigDecimal.ZERO,

    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),
    val payments: List<PaymentDocument> = emptyList(),

    val soIds: List<String> = emptyList(),
    val soPreviews: Map<String, DenormalizedServiceOrderDescDocument> = emptyMap(),

    val legalText: String = "",
    val legalVersion: String = "",

    val hasProductWithPendingCheck: Boolean = false,
    val daysToTakeFromStore: Int = 0,

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
) {
    val isPaymentFull = totalPaid.setScale(2, RoundingMode.HALF_EVEN) >=
            finalPrice.setScale(2, RoundingMode.HALF_EVEN)
}
