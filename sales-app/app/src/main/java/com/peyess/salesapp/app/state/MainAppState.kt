package com.peyess.salesapp.app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.database.room.gambeta.GambetaEntity

sealed class AppAuthenticationState {
    object Unauthenticated: AppAuthenticationState()
    object Authenticated: AppAuthenticationState()
    object Away: AppAuthenticationState()
}

data class MainAppState(
    val authState: AppAuthenticationState = AppAuthenticationState.Unauthenticated,

    val createNewSale: Async<Boolean> = Success(false),

    val isUpdatingProductsAsync: Async<GambetaEntity?> = Uninitialized,
): MavericksState {
    val isCreatingNewSale = createNewSale is Loading

    val isUpdatingProducts = if (isUpdatingProductsAsync is Success) {
        isUpdatingProductsAsync.invoke()?.isUpdating ?: true
    } else {
        true
    }
}
