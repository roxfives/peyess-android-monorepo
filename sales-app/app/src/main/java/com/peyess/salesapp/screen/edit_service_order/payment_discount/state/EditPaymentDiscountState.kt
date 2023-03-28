package com.peyess.salesapp.screen.edit_service_order.payment_discount.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.feature.payment_discount.model.Discount
import com.peyess.salesapp.feature.payment_discount.model.group.DiscountGroup
import java.math.BigDecimal

data class EditPaymentDiscountState(
    val saleId: String = "",
    val fullPrice: BigDecimal = BigDecimal(0.0),

    val discountResponseAsync: Async<EditPaymentDiscountFetchResponse> = Uninitialized,
    val currentDiscount: Discount = Discount(),

    val discountGroupDocumentAsync: Async<DiscountGroupDocument?> = Uninitialized,
    val discountGroup: DiscountGroup = DiscountGroup(),

    val pricePreview: BigDecimal = BigDecimal(0),
): MavericksState
