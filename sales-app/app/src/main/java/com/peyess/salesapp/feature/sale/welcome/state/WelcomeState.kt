package com.peyess.salesapp.feature.sale.welcome.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.model.users.Collaborator

data class WelcomeState(
    val collaborator: Async<Collaborator> = Uninitialized,
    val goNextAttempts: Int = 0,
    val clientName: String = "",
): MavericksState {
    val isLoading = collaborator is Loading

    val hasError = goNextAttempts > 0 && clientName.isEmpty()
    val canGoNext = clientName.isNotBlank()
}