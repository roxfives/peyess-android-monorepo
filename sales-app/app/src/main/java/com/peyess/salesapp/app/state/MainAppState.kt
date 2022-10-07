package com.peyess.salesapp.app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.database.room.gambeta.GambetaEntity
import com.peyess.salesapp.navigation.pick_client.PickScenario

sealed class AppAuthenticationState {
    object Unauthenticated: AppAuthenticationState()
    object Authenticated: AppAuthenticationState()
    object Away: AppAuthenticationState()
}

data class MainAppState(
    val authState: AppAuthenticationState = AppAuthenticationState.Unauthenticated,

    val createNewSale: Async<Boolean> = Success(false),

    val isUpdatingProductsAsync: Async<GambetaEntity?> = Uninitialized,

    val serviceOrderListAsync: Async<List<ServiceOrderDocument>> = Uninitialized,

    // Client screen
    val clientListAsync: Async<List<ClientDocument>> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",
): MavericksState {
    val isCreatingNewSale = createNewSale is Loading

    val isUpdatingProducts = if (isUpdatingProductsAsync is Success) {
        isUpdatingProductsAsync.invoke()?.isUpdating ?: true
    } else {
        true
    }

    // Client screen
    val clientList = clientListAsync.invoke() ?: emptyList()

    val isServiceOrderListLoading = serviceOrderListAsync is Loading
    val serviceOrderList = serviceOrderListAsync.invoke() ?: emptyList()

    val areClientsLoading = clientListAsync is Loading || clientListAsync is Uninitialized
}
