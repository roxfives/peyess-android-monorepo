package com.peyess.salesapp.feature.sale.pick_client.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse

data class PickClientState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val activeServiceOrderResponse: ActiveSOEntity = ActiveSOEntity(),

    val clientListAsync: Async<List<ClientDocument>> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",
): MavericksState {
    val saleId: String = activeServiceOrderResponse.saleId
    val serviceOrderId: String = activeServiceOrderResponse.id

    val clientList = clientListAsync.invoke() ?: emptyList()

    val isLoading = clientListAsync is Loading || clientListAsync is Uninitialized
}
