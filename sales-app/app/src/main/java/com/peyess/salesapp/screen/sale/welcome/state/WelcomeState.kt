package com.peyess.salesapp.screen.sale.welcome.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.CancelSaleResponse

data class WelcomeState(
    val collaboratorNameAsync: Async<String> = Uninitialized,
    val collaboratorName: String = "",

    val serviceOrderResponse: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderId: String = "",
    val clientNameInput: String = "",

    val cancelCurrentSaleAsync: Async<CancelSaleResponse> = Uninitialized,

    val hasFinished: Boolean = false,
): MavericksState {
    val isLoading = collaboratorNameAsync is Loading
            || serviceOrderResponse is Loading

    val canGoNext = clientNameInput.isNotBlank()
}