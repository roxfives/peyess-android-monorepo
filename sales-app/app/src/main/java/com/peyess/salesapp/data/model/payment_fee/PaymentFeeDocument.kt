package com.peyess.salesapp.data.model.payment_fee

import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal

data class PaymentFeeDocument(
    val saleId: String = "",
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.None,
    val value: BigDecimal = BigDecimal.ZERO,
)

fun PaymentFeeDocument.toWholeFormat(
    priceWithDiscount: BigDecimal,
): PaymentFeeDocument {
    when (method) {
        is PaymentFeeCalcMethod.Percentage -> {
            val wholeValue = priceWithDiscount.multiply(value)

            return copy(
                method = PaymentFeeCalcMethod.Whole,
                value = wholeValue,
            )
        }

        is PaymentFeeCalcMethod.Whole -> {
            return copy()
        }

        is PaymentFeeCalcMethod.None -> {
            return copy(
                method = PaymentFeeCalcMethod.Whole,
                value = BigDecimal.ZERO,
            )
        }
    }
}

fun PaymentFeeDocument.toPercentageFormat(
    priceWithDiscount: BigDecimal
): PaymentFeeDocument {
    when (method) {
        is PaymentFeeCalcMethod.Percentage -> {
            return copy()
        }

        is PaymentFeeCalcMethod.Whole -> {
            val percentageValue = value.divide(priceWithDiscount)

            return copy(
                method = PaymentFeeCalcMethod.Percentage,
                value = percentageValue,
            )
        }

        is PaymentFeeCalcMethod.None -> {
            return copy(
                method = PaymentFeeCalcMethod.Percentage,
                value = BigDecimal(0)
            )
        }
    }
}