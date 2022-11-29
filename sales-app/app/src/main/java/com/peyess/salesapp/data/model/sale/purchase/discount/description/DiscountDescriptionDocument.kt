package com.peyess.salesapp.data.model.sale.purchase.discount.description

import com.peyess.salesapp.typing.products.DiscountCalcMethod
import java.math.BigDecimal

data class DiscountDescriptionDocument(
    val method: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    val value: BigDecimal = BigDecimal(0.0),
)