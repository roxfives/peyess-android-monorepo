package com.peyess.salesapp.feature.sale.pick_client.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client.toClientPickedEntity
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
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
        loadServiceOrderData()

        onAsync(PickClientState::activeServiceOrderResponseAsync) { processServiceOrderDataResponse(it) }
    }

    private fun loadClients() = withState {
        clientRepository.clients()
            .execute {
                copy(clientListAsync = it)
            }
    }

    private fun processServiceOrderDataResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    activeServiceOrderResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(activeServiceOrderResponse = it) }
        )
    }

    private fun loadServiceOrderData() {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute(Dispatchers.IO) {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun pickAllForServiceOrder(client: ClientDocument) = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .execute(Dispatchers.IO) { so ->
                if (so is Success) {
                    saleRepository.pickClient(client.toClientPickedEntity(so.invoke().id, ClientRole.User))
                    saleRepository.pickClient(client.toClientPickedEntity(so.invoke().id, ClientRole.Responsible))

                    copy(hasPickedClient = true, pickedId = client.id)
                } else {

                    copy(hasPickedClient = false, pickedId = client.id)
                }
            }
    }

    private fun pickOnlyOne(client: ClientDocument, role: ClientRole) = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .execute(Dispatchers.IO) { so ->
                if (so is Success) {
                    saleRepository.pickClient(client.toClientPickedEntity(so.invoke().id, role))

                    copy(hasPickedClient = true, pickedId = client.id)
                } else {

                    copy(hasPickedClient = false, pickedId = client.id)
                }
            }
    }

    private fun pickIdOnly(client: ClientDocument) = setState {
        copy(hasPickedClient = true, pickedId = client.id)
    }

    fun updatePickScenario(scenario: PickScenario) = setState {
        copy(pickScenario = scenario)
    }

    fun pickClient(client: ClientDocument) = withState {
        when (it.pickScenario) {
            PickScenario.ServiceOrder -> pickAllForServiceOrder(client)
            PickScenario.Responsible -> pickOnlyOne(client, ClientRole.Responsible)
            PickScenario.User -> pickOnlyOne(client, ClientRole.User)
            PickScenario.Witness -> pickOnlyOne(client, ClientRole.Witness)
            else -> pickIdOnly(client)
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