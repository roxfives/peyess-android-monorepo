package com.peyess.salesapp.features.pdf.service_order.utils

import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.payment_fee.toWholeFormat
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

fun calculateDiscount(
    discount: DiscountDescriptionDocument,
    priceWithoutDiscount: BigDecimal,
): BigDecimal {
    return when (discount.method) {
        DiscountCalcMethod.Whole -> priceWithoutDiscount.subtract(discount.value)
        DiscountCalcMethod.Percentage -> priceWithoutDiscount
            .multiply(
                BigDecimal(1)
                .subtract(discount.value))
        DiscountCalcMethod.None -> priceWithoutDiscount
    }
}

fun joinFees(
    a: PaymentFeeDocument,
    b: PaymentFeeDocument,
    priceWithDiscount: BigDecimal,
): PaymentFeeDocument {
    val feeAAsWhole = a.toWholeFormat(priceWithDiscount)
    val feeBAsWhole = b.toWholeFormat(priceWithDiscount)

    return joinWholeFees(feeAAsWhole, feeBAsWhole)
}

private fun joinWholeFees(
    a: PaymentFeeDocument,
    b: PaymentFeeDocument,
): PaymentFeeDocument {
    return PaymentFeeDocument(
        saleId = a.saleId,
        method = a.method,
        value = a.value + b.value,
    )
}

fun joinDiscounts(
    a: DiscountDescriptionDocument,
    b: DiscountDescriptionDocument,
    priceWithoutDiscount: BigDecimal,
): DiscountDescriptionDocument {
    val discountA = if (a.method == DiscountCalcMethod.None) {
        DiscountDescriptionDocument(
            method = DiscountCalcMethod.Percentage,
            value = BigDecimal.ZERO,
        )
    } else {
        a
    }

    val discountB = if (b.method == DiscountCalcMethod.None) {
        DiscountDescriptionDocument(
            method = DiscountCalcMethod.Percentage,
            value = BigDecimal.ZERO,
        )
    } else {
        b
    }

    if (discountA.method == DiscountCalcMethod.Whole) {
        return if (discountB.method == DiscountCalcMethod.Whole) {
            DiscountDescriptionDocument(
                method = DiscountCalcMethod.Whole,
                value = discountA.value + discountB.value,
            )
        } else {
            joinIncompatibleDiscounts(
                discountAsWhole = discountA,
                discountAsPercentage = discountB,
                priceWithoutDiscount = priceWithoutDiscount,
            )
        }
    } else {
        return if (discountB.method == DiscountCalcMethod.Whole) {
            joinIncompatibleDiscounts(
                discountAsWhole = discountB,
                discountAsPercentage = discountA,
                priceWithoutDiscount = priceWithoutDiscount,
            )
        } else {
            joinPercentageDiscounts(
                a = discountA,
                b = discountB,
                priceWithoutDiscount = priceWithoutDiscount,
            )
        }
    }
}

private fun joinPercentageDiscounts(
    a: DiscountDescriptionDocument,
    b: DiscountDescriptionDocument,
    priceWithoutDiscount: BigDecimal,
): DiscountDescriptionDocument {
    if (a.value.compareTo(b.value) == 0) {
        return DiscountDescriptionDocument(
            method = DiscountCalcMethod.Percentage,
            value = a.value,
        )
    } else {
        val aDiscountAsWhole = a.value * priceWithoutDiscount
        val bDiscountAsWhole = b.value * priceWithoutDiscount
        val totalDiscount = aDiscountAsWhole + bDiscountAsWhole

        val totalDiscountAsPercentage = totalDiscount / priceWithoutDiscount

        return DiscountDescriptionDocument(
            method = DiscountCalcMethod.Percentage,
            value = totalDiscountAsPercentage,
        )
    }
}

private fun joinIncompatibleDiscounts(
    discountAsWhole: DiscountDescriptionDocument,
    discountAsPercentage: DiscountDescriptionDocument,
    priceWithoutDiscount: BigDecimal,
): DiscountDescriptionDocument {
    val percentageAsWhole = discountAsPercentage.value * priceWithoutDiscount

    return DiscountDescriptionDocument(
        method = DiscountCalcMethod.Whole,
        value = percentageAsWhole + discountAsWhole.value,
    )
}