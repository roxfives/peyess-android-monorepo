package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetchResponse

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val saleId: String = "",

    val serviceOrderFetchResponse: Async<ServiceOrderFetchResponse> = Uninitialized,
): MavericksState
