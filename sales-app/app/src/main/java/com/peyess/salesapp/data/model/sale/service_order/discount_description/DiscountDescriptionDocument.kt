package com.peyess.salesapp.data.model.sale.service_order.discount_description

import com.peyess.salesapp.typing.products.DiscountCalcMethod

data class DiscountDescriptionDocument(
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    val value: Double = 0.0,
)