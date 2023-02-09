package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.MavericksState

data class EditServiceOrderState(
    val serviceOrderId: String = "",
    val purchaseId: String = "",
): MavericksState
