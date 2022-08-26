package com.peyess.salesapp.feature.sale.service_order.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.room.ClientEntity

data class ServiceOrderState(
    val userClientAsync: Async<ClientEntity?> = Uninitialized,
    val responsibleClientAsync: Async<ClientEntity?> = Uninitialized,
    val witnessClientAsync: Async<ClientEntity?> = Uninitialized,
): MavericksState {
    val isUserLoading = userClientAsync is Loading
    val userClient = if (userClientAsync is Success) {
        userClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isResponsibleLoading = responsibleClientAsync is Loading
    val responsibleClient = if (responsibleClientAsync is Success) {
        responsibleClientAsync.invoke() ?: ClientEntity()
    } else {
        ClientEntity()
    }

    val isWitnessLoading = witnessClientAsync is Loading
    val witnessClient = witnessClientAsync.invoke()
}
