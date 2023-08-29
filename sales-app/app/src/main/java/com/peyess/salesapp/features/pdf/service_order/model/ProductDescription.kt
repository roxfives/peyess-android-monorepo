package com.peyess.salesapp.features.pdf.service_order.model

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.payment_fee.toWholeFormat
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.toWholeFormat
import java.math.BigDecimal

data class ProductDescription(
    val productCode: String = "",
    val quantity: Int = 0,
    val description: String = "",
    val priceWithoutDiscount: BigDecimal = BigDecimal("0"),
    val discount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val fee: PaymentFeeDocument = PaymentFeeDocument(),
) {
    val priceWithDiscount = priceWithoutDiscount -
            discount.toWholeFormat(priceWithoutDiscount).value
    val priceWithFee = priceWithDiscount +
            fee.toWholeFormat(priceWithDiscount).value
}

fun MutableList<ProductDescription>.sortedByQuantity(): List<ProductDescription> {
    return this.toMutableList().sortedBy { it.quantity }
}
