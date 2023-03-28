package com.peyess.salesapp.feature.payment_discount.model

import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod

fun OverallDiscountDocument.toDiscount(): Discount {
    return when (discountMethod) {
        DiscountCalcMethod.Percentage ->
            Discount(
                method = discountMethod,
                percentValue = overallDiscountValue,
            )

        DiscountCalcMethod.Whole ->
            Discount(
                method = discountMethod,
                wholeValue = overallDiscountValue,
            )

        DiscountCalcMethod.None ->
            Discount(method = discountMethod)
    }
}

fun Discount.toOverallDiscountDocument(saleId: String): OverallDiscountDocument {
    return OverallDiscountDocument(
        saleId = saleId,
        discountMethod = method,
        overallDiscountValue = value
    )
}