package com.peyess.salesapp.screen.sale.pick_client.state

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.work.ExistingWorkPolicy
import arrow.core.Either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.screen.sale.pick_client.adapter.toClient
import com.peyess.salesapp.screen.sale.pick_client.adapter.toClientPickedEntity
import com.peyess.salesapp.screen.sale.pick_client.model.Client
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.workmanager.clients.enqueueOneTimeClientDownloadWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import timber.log.Timber

private typealias ViewModelFactory = MavericksViewModelFactory<PickClientViewModel, PickClientState>

private const val clientsTablePageSize = 20
private const val lensesTablePrefetchDistance = 10

class PickClientViewModel @AssistedInject constructor(
    @Assisted initialState: PickClientState,
    private val saleRepository: SaleRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
    private val clientRepository: ClientRepository,
    private val localClientRepository: LocalClientRepository,
): MavericksViewModel<PickClientState>(initialState) {

    init {
        updateClientList()
        loadServiceOrderData()

        onAsync(PickClientState::existingCreateClientAsync) { processActiveCreatingClientResponse(it) }
        onAsync(PickClientState::createClientResponseAsync) { processCreateNewClientResponse(it) }

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

    @OptIn(ExperimentalCoroutinesApi::class)
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

        return localClientRepository.paginateClients(query).map { pagingSourceFactory ->
            val pager = Pager(
                pagingSourceFactory = pagingSourceFactory,
                config = PagingConfig(
                    pageSize = clientsTablePageSize,
                    enablePlaceholders = true,
                    prefetchDistance = lensesTablePrefetchDistance,
                ),
            )

            pager.flow.cancellable().cachedIn(viewModelScope).mapLatest { pagingData ->
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

    private fun setExistingCreateClient(client: CacheCreateClientDocument) = setState {
        copy(existingCreateClient = client)
    }

    private fun processActiveCreatingClientResponse(
        response: CacheCreateClientFetchSingleResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                setExistingCreateClient(CacheCreateClientDocument())

                copy(
                    hasLookedForExistingClient = true,
                )
            },

            ifRight = {
                copy(
                    existingCreateClient = it,
                    hasLookedForExistingClient = true,
                )
            },
        )
    }

    private fun processCreateNewClientResponse(
        response: CacheCreateClientCreateResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    createClientResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(
                    createClientId = it.id,
                    createClient = true,
                )
            }
        )
    }

    fun syncClients(context: Context) {
        enqueueOneTimeClientDownloadWorker(
            context = context,
            forceSync = true,
            workPolicy = ExistingWorkPolicy.REPLACE,
        )
    }

    suspend fun pictureForClient(clientId: String): Uri {
        return Either.catch {
            clientRepository.pictureForClient(clientId)
        }.mapLeft {
            Timber.e("Error getting picture for client $clientId: ${it.message}", it)
        }.fold(
            ifLeft = { Uri.EMPTY },
            ifRight = { it },
        )
    }

    fun findActiveCreatingClient() {
        suspend {
            cacheCreateClientRepository.findCreating()
        }.execute {
            copy(existingCreateClientAsync = it)
        }
    }

    fun createNewClient() = withState {
        suspend {
            if (it.creatingClientExists) {
                val existingClient = it.existingCreateClient.copy(
                    isCreating = false,
                )

                cacheCreateClientRepository.update(existingClient)
            }

            cacheCreateClientRepository.createClient()
        }.execute(Dispatchers.IO) {
            copy(createClientResponseAsync = it)
        }
    }



    fun createClientFromCache() = setState {
        copy(
            createClientId = existingCreateClientId,
            createClient = true,
        )
    }

    fun checkedForExistingClient() = setState {
        copy(hasLookedForExistingClient = false)
    }

    fun startedCreatingClient() = setState {
        copy(
            hasLookedForExistingClient = false,
            createClient = false,
            createClientId = "",
        )
    }

    fun updatePickScenario(scenario: PickScenario) = setState {
        copy(pickScenario = scenario)
    }

    fun updatePaymentId(paymentId: Long) = setState {
        copy(paymentId = paymentId)
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