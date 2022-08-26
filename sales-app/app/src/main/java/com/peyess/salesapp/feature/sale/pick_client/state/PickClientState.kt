package com.peyess.salesapp.feature.sale.pick_client.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.Client

data class PickClientState(
    val clientListAsync: Async<List<Client>> = Uninitialized,

    val hasPickedClient: Boolean = false,
): MavericksState {
    val clientList = clientListAsync.invoke() ?: emptyList()

    val isLoading = clientListAsync is Loading || clientListAsync is Uninitialized
}
