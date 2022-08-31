package com.peyess.salesapp.feature.sale.pick_client.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.client.room.toEntity
import com.peyess.salesapp.repository.clients.ClientRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take

class PickClientViewModel @AssistedInject constructor(
    @Assisted initialState: PickClientState,
    private val clientRepository: ClientRepository,
    private val saleRepository: SaleRepository,
): MavericksViewModel<PickClientState>(initialState) {

    init {
        loadClients()
    }

    private fun loadClients() = withState {
        clientRepository.clients()
            .execute {
                copy(clientListAsync = it)
            }
    }

    fun pickClient(client: ClientDocument) = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .execute(Dispatchers.IO) { so ->
                if (so is Success) {
                    saleRepository.pickClient(client.toEntity(so.invoke().id, ClientRole.User))
                    saleRepository.pickClient(client.toEntity(so.invoke().id, ClientRole.Responsible))

                    copy(hasPickedClient = true)
                } else {

                    copy(hasPickedClient = false)
                }
            }
    }

    fun clientPicked() = setState {
        copy(hasPickedClient = false)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PickClientViewModel, PickClientState> {
        override fun create(state: PickClientState): PickClientViewModel
    }

    companion object:
        MavericksViewModelFactory<PickClientViewModel, PickClientState> by hiltMavericksViewModelFactory()
}