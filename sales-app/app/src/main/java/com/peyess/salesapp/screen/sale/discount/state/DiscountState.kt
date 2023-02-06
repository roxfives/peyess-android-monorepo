package com.peyess.salesapp.screen.sale.discount.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.screen.sale.discount.model.Discount
import com.peyess.salesapp.screen.sale.discount.model.group.DiscountGroup
import com.peyess.salesapp.screen.sale.discount.model.group.toDiscountGroup
import com.peyess.salesapp.screen.sale.discount.model.toDiscount
import java.math.BigDecimal

data class DiscountState(
    val currentDiscountAsync: Async<OverallDiscountDocument?> = Uninitialized,
    val discountGroupDocumentAsync: Async<DiscountGroupDocument?> = Uninitialized,

    val originalPrice: BigDecimal = BigDecimal(0),
    val pricePreview: BigDecimal = BigDecimal(0),

    val saleId: String = "",
): MavericksState {
    val isDiscountLoading: Boolean = currentDiscountAsync is Loading
    val discountHasError: Boolean = currentDiscountAsync is Fail
    val discount: Discount = currentDiscountAsync.invoke()?.toDiscount()?.copy() ?: Discount()

    val isDiscountGroupLoading: Boolean = discountGroupDocumentAsync is Loading
    val discountGroupHasError: Boolean = discountGroupDocumentAsync is Fail
    val discountGroup: DiscountGroup = discountGroupDocumentAsync.invoke()
        ?.toDiscountGroup()
        ?: DiscountGroup()
}