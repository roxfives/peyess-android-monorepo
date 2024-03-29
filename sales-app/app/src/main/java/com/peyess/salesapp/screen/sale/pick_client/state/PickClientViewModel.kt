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
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client_legal.ClientLegalDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientInsertResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.client.ClientAndLegalResponse
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedFetchAllResponse
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_sale.client_picked.AllClientsPickedResponse
import com.peyess.salesapp.data.repository.local_sale.client_picked.ClientPickedRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.screen.sale.pick_client.adapter.toClient
import com.peyess.salesapp.screen.sale.pick_client.adapter.toClientPickedEntity
import com.peyess.salesapp.feature.client_list.model.Client
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.screen.sale.pick_client.adapter.toEditClientPickedDocument
import com.peyess.salesapp.utils.string.removeDiacritics
import com.peyess.salesapp.utils.string.removePonctuation
import com.peyess.salesapp.workmanager.clients.enqueueOneTimeClientDownloadWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private typealias ViewModelFactory = AssistedViewModelFactory<PickClientViewModel, PickClientState>
private typealias PickClientViewModelFactory =
        MavericksViewModelFactory<PickClientViewModel, PickClientState>

private const val clientsTablePageSize = 20
private const val lensesTablePrefetchDistance = 10

private const val clientSearchDebounceTime = 500

class PickClientViewModel @AssistedInject constructor(
    @Assisted initialState: PickClientState,
    private val saleRepository: SaleRepository,
    private val editClientPickedRepository: EditClientPickedRepository,
    private val clientPickedRepository: ClientPickedRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
    private val clientRepository: ClientRepository,
    private val localClientRepository: LocalClientRepository,
): MavericksViewModel<PickClientState>(initialState) {
    private val clientSearchState = MutableStateFlow("")

    init {
        updateClientList()

        onEach(PickClientState::clientSearchQuery) { clientSearchState.value = it }

        onEach(PickClientState::clientUidHighlightList) { loadHighlightClients(it) }

        onEach(
            PickClientState::pickScenario,
            PickClientState::serviceOrderId,
        ) { scenario, serviceOrderId ->
            when (scenario) {
                PickScenario.EditPayment,
                PickScenario.EditResponsible,
                PickScenario.EditUser,
                PickScenario.EditWitness -> {
                    loadMainEditClients(serviceOrderId)
                }

                PickScenario.Payment,
                PickScenario.Responsible,
                PickScenario.ServiceOrder,
                PickScenario.User,
                PickScenario.Witness -> {
                    loadMainClients(serviceOrderId)
                }
            }
        }

        onAsync(PickClientState::existingCreateClientAsync) { processActiveCreatingClientResponse(it) }
        onAsync(PickClientState::createClientResponseAsync) { processCreateNewClientResponse(it) }

        onAsync(PickClientState::clientListResponseAsync) { processClientListResponse(it) }

        onAsync(PickClientState::editClientResponseAsync) { processEditClientResponse(it) }
        onAsync(PickClientState::cacheCreateClientInsertResponseAsync) {
            processAddClientToCacheResponse(it)
        }

        onAsync(PickClientState::clientListSearchAsync) { processSearchClientListResponse(it) }

        onAsync(PickClientState::editClientPickedHighlightListAsync) {
            processMainEditClientsResponse(it)
        }
        onAsync(PickClientState::clientPickedHighlightListAsync) {
            processMainClientsResponse(it)
        }

        onAsync(PickClientState::clientHighlightListAsync) { processClientHighlightResponse(it) }

        observeClientSearch()
    }

    @OptIn(FlowPreview::class)
    private fun observeClientSearch() {
        viewModelScope.launch {
            val duration = clientSearchDebounceTime.toDuration(DurationUnit.MILLISECONDS)

            clientSearchState.debounce(duration).collect {
                searchClient(it)
            }
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

    private fun editOnlyOne(client: Client, role: ClientRole) = withState {
        suspend {
            editClientPickedRepository.insertClientPicked(
                clientPicked = client.toEditClientPickedDocument(
                    serviceOrderId = it.serviceOrderId,
                    clientRole = role,
                )
            )
        }.execute(Dispatchers.IO) {
            copy(hasPickedClient = it is Success, pickedId = client.id)
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

    private fun processEditClientResponse(
        response: ClientAndLegalResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    editClientResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = { (client, legal) ->
                addClientToCache(client, legal)
                copy(updateClientId = client.id)
            }
        )
    }

    private fun addClientToCache(client: ClientDocument, legal: ClientLegalDocument) {
        suspend {
            val cacheClient = client.toCacheCreateClientDocument(
                hasAcceptedPromotionalMessages = legal.hasAcceptedPromotionalMessages,
            )

            cacheCreateClientRepository.insertClient(cacheClient)
        }.execute(Dispatchers.IO) {
            copy(cacheCreateClientInsertResponseAsync = it)
        }
    }

    private fun processAddClientToCacheResponse(
        response: CacheCreateClientInsertResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    cacheCreateClientInsertResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(updateClient = true)
            }
        )
    }

    private fun searchClient(query: String) {
        suspend {
            updatedClientSearchListStream(
                query.lowercase().removeDiacritics().removePonctuation()
            )
        }.execute(Dispatchers.IO) {
            copy(clientListSearchAsync = it)
        }
    }

    private fun processSearchClientListResponse(response: ClientsListResponse) = setState {
        response.fold(
            ifLeft = {
                copy(clientListSearchAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = { copy(clientListSearchStream = it) }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun updatedClientSearchListStream(queryStr: String): ClientsListResponse {
        // TODO: build query using utils
        val query = PeyessQuery(
            queryFields = listOf(
                buildQueryField(
                    field = "searchable",
                    op = PeyessQueryOperation.Like,
                    value = queryStr,
                ),
            ),
            orderBy = listOf(
                PeyessOrderBy(
                    field = "name",
                    order = Order.ASCENDING,
                ),
            ),
            groupBy = emptyList(),
            defaultOp = "OR"
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

    private fun loadMainEditClients(serviceOrderId: String) {
        suspend {
            editClientPickedRepository.allClientsForServiceOrder(serviceOrderId)
        }.execute {
            copy(editClientPickedHighlightListAsync = it)
        }
    }

    private fun processMainEditClientsResponse(
        response: EditClientPickedFetchAllResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(editClientPickedHighlightListAsync = Fail(it.error))
            },

            ifRight = {
                copy(clientUidHighlightList = it.map { c -> c.id })
            }
        )
    }

    private fun loadMainClients(serviceOrderId: String) {
        suspend {
            clientPickedRepository.allClientsForServiceOrder(serviceOrderId)
        }.execute {
            copy(clientPickedHighlightListAsync = it)
        }
    }

    private fun processMainClientsResponse(
        response: AllClientsPickedResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(editClientPickedHighlightListAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(clientUidHighlightList = it.map { c -> c.id })
            }
        )
    }

    private fun loadHighlightClients(uids: List<String>) {
        suspend {
            uids.toSet().map { localClientRepository.clientById(it) }.mapNotNull { response ->
                response.fold(ifLeft = { null }, ifRight = { it })
            }.map { it.toClient() }
        }.execute {
            copy(clientHighlightListAsync = it)
        }
    }

    private fun processClientHighlightResponse(clients: List<Client>) = setState {
        copy(clientHighlightList = clients)
    }

    fun clearClientSearch() = setState {
        copy(clientSearchQuery = "")
    }

    fun startClientSearch() = setState {
        copy(isSearchActive = true)
    }

    fun stopClientSearch() = setState {
        copy(
            isSearchActive = false,
            clientSearchQuery = "",
        )
    }

    fun onClientSearchChanged(query: String) = setState {
        copy(clientSearchQuery = query)
    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
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

            PickScenario.Payment -> pickIdOnly(client)
            PickScenario.User -> pickOnlyOne(client, ClientRole.User)
            PickScenario.Responsible -> pickOnlyOne(client, ClientRole.Responsible)
            PickScenario.Witness -> pickOnlyOne(client, ClientRole.Witness)

            PickScenario.EditPayment -> pickIdOnly(client)
            PickScenario.EditUser -> editOnlyOne(client, ClientRole.User)
            PickScenario.EditResponsible -> editOnlyOne(client, ClientRole.Responsible)
            PickScenario.EditWitness -> editOnlyOne(client, ClientRole.Witness)
        }
    }

    fun clientPicked() = setState {
        copy(hasPickedClient = false)
    }

    fun loadEditClientToCache(clientId: String) {
        suspend {
            clientRepository.clientAndLegalById(clientId)
        }.execute {
            copy(editClientResponseAsync = it)
        }
    }

    fun startedUpdatingClient() = setState {
        copy(
            updateClient = false,
            updateClientId = "",
            cacheCreateClientInsertResponseAsync = Uninitialized,
            editClientResponseAsync = Uninitialized,
        )
    }

    // hilt
    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: PickClientState): PickClientViewModel
    }

    companion object: PickClientViewModelFactory by hiltMavericksViewModelFactory()
}