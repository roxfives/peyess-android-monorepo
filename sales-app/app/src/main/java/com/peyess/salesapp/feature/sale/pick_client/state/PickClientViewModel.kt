package com.peyess.salesapp.feature.sale.pick_client.state

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.pick_client.adapter.toClient
import com.peyess.salesapp.feature.sale.pick_client.adapter.toClientPickedEntity
import com.peyess.salesapp.feature.sale.pick_client.model.Client
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

private typealias ViewModelFactory = MavericksViewModelFactory<PickClientViewModel, PickClientState>

private const val clientsTablePageSize = 20
private const val lensesTablePrefetchDistance = 10

class PickClientViewModel @AssistedInject constructor(
    @Assisted initialState: PickClientState,
    private val saleRepository: SaleRepository,
    private val localClientRepository: LocalClientRepository,
): MavericksViewModel<PickClientState>(initialState) {

    init {
        updateClientList()
        loadServiceOrderData()

        onAsync(PickClientState::clientListResponseAsync) { processClientListResponse(it) }

        onAsync(PickClientState::activeServiceOrderResponseAsync) {
            processServiceOrderDataResponse(it)
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

    private fun pickAllForServiceOrder(client: Client) = withState {
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

    private fun pickOnlyOne(client: Client, role: ClientRole) = withState {
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

    private fun updatedClientListStream(): ClientsListResponse {
        // TODO: build query using utils
        val query = PeyessQuery(
            queryFields = emptyList(),
            orderBy = listOf(
                PeyessOrderBy(
                    field = "name",
                    order = Order.ASCENDING,
                )
            ),
            groupBy = emptyList(),
        )

        return localClientRepository.paginateClients(query).map { pagingSource ->
            val pagingSourceFactory = { pagingSource }

            val pager = Pager(
                pagingSourceFactory = pagingSourceFactory,
                config = PagingConfig(
                    pageSize = clientsTablePageSize,
                    enablePlaceholders = true,
                    prefetchDistance = lensesTablePrefetchDistance,
                ),
            )

            pager.flow.map { pagingData ->
                pagingData.map { it.toClient() }
            }
        }
    }

    private fun pickIdOnly(client: Client) = setState {
        copy(hasPickedClient = true, pickedId = client.id)
    }

    private fun processClientListResponse(response: ClientsListResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    clientListResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },
            ifRight = {
                copy(clientListStream = it)
            }
        )
    }

    private fun updateClientList() {
        suspend {
            updatedClientListStream()
        }.execute(Dispatchers.IO) {
            copy(clientListResponseAsync = it)
        }
    }

    fun updatePickScenario(scenario: PickScenario) = setState {
        copy(pickScenario = scenario)
    }

    fun pickClient(client: Client) = withState {
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

    companion object: ViewModelFactory by hiltMavericksViewModelFactory()
}