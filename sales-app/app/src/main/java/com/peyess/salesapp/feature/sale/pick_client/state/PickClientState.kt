package com.peyess.salesapp.feature.sale.pick_client.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.navigation.pick_client.PickScenario

data class PickClientState(
    val clientListAsync: Async<List<ClientDocument>> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",
): MavericksState {
    val clientList = clientListAsync.invoke() ?: emptyList()

    val isLoading = clientListAsync is Loading || clientListAsync is Uninitialized
}
