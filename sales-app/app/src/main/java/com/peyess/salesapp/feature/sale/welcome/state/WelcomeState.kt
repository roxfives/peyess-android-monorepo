package com.peyess.salesapp.feature.sale.welcome.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.model.users.CollaboratorDocument

data class WelcomeState(
    val collaboratorDocument: Async<CollaboratorDocument> = Uninitialized,
    val activeSO: Async<ActiveSOEntity?> = Uninitialized,

    val goNextAttempts: Int = 0,
    val hasUpdatedSale: Boolean = false,
): MavericksState {
    val isLoading = collaboratorDocument is Loading || collaboratorDocument is Uninitialized
            || activeSO is Loading || activeSO is Uninitialized

    private val _activeSO: ActiveSOEntity? = activeSO.invoke()
    val clientName = if (activeSO is Success && _activeSO != null) {
        _activeSO.clientName
    } else {
        ""
    }

    val hasError = goNextAttempts > 0 && clientName.isEmpty()
    val canGoNext = clientName.isNotBlank()
}