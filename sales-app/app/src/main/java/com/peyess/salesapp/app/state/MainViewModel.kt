package com.peyess.salesapp.app.state

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.work.ExistingWorkPolicy
import arrow.core.Either
import arrow.core.continuations.either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.app.adapter.toClient
import com.peyess.salesapp.app.adapter.toUnfinishedSale
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.db_view.ServiceOrderDBView
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.client_legal.ClientLegalDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientInsertResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.client.ClientAndLegalResponse
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientTotalResponse
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.repository.sale.ActiveSalesStreamResponse
import com.peyess.salesapp.repository.sale.CreateSaleResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.screen.home.model.UnfinishedSale
import com.peyess.salesapp.utils.file.createPrintFile
import com.peyess.salesapp.utils.string.removeDiacritics
import com.peyess.salesapp.utils.string.removePonctuation
import com.peyess.salesapp.workmanager.clients.enqueueOneTimeClientDownloadWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.nvest.html_to_pdf.HtmlToPdfConvertor
import timber.log.Timber
import java.io.File
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val purchasesTablePageSize = 20
private const val clientsTablePageSize = 20
private const val lensesTablePrefetchDistance = 10

private const val clientSearchDebounceTime = 500

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val saleRepository: SaleRepository,
    private val purchaseRepository: PurchaseRepository,
    private val productsTableStateRepository: ProductsTableStateRepository,
    private val localClientRepository: LocalClientRepository,
    private val clientRepository: ClientRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
): MavericksViewModel<MainAppState>(initialState) {
    private val clientSearchState = MutableStateFlow("")

    init {
        streamUnfinishedSales()

        onEach(MainAppState::authState) {
            if (it is AppAuthenticationState.Authenticated) {
                countTotalClients()
                updateClientList()
                loadPurchases()
            }
        }

        authenticationRepository.storeAuthState.setOnEach {
            when (it) {
                is StoreAuthState.Authenticated ->
                    copy(authState = AppAuthenticationState.Authenticated)
                is StoreAuthState.Unauthenticated ->
                    copy(authState = AppAuthenticationState.Unauthenticated)
//                else ->
//                    copy(authState = AppAuthenticationState.Away)

            }
        }

        onEach(MainAppState::unfinishedSalesStream) { processUnfinishedSaleStreamResponse(it) }

        onAsync(MainAppState::createSaleResponseAsync) { processCreateSaleResponse(it) }

        onEach(MainAppState::clientSearchQuery) { clientSearchState.value = it }

        onAsync(MainAppState::purchaseListResponseAsync) {
            processServiceOrderListResponse(it)
        }

        onAsync(MainAppState::existingCreateClientAsync) { processActiveCreatingClientResponse(it) }
        onAsync(MainAppState::createClientResponseAsync) { processCreateNewClientResponse(it) }
        onAsync(MainAppState::totalClientsResponseAsync) { processTotalClientResponse(it) }

        onAsync(MainAppState::clientListResponseAsync) { processClientListResponse(it) }

        onAsync(MainAppState::unfinishedSalesStreamAsync) { processActiveSalesStreamResponse(it) }
        onAsync(MainAppState::unfinishedSalesAsync) { updateUnfinishedSales(it) }

        onAsync(MainAppState::editClientResponseAsync) { processEditClientResponse(it) }
        onAsync(MainAppState::cacheCreateClientInsertResponseAsync) {
            processAddClientToCacheResponse(it)
        }

        onAsync(MainAppState::clientListSearchAsync) { processSearchClientListResponse(it) }

        loadProductTableStatus()
        loadCurrentCollaborator()
        loadStore()

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

    private fun loadProductTableStatus() {
        productsTableStateRepository.observeState().execute(Dispatchers.IO) {
            copy(productsTableStatusAsync = it)
        }
    }

    private fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store $it")
            copy(currentStoreAsync = it)
        }
    }

    private fun loadCurrentCollaborator() = withState {
        authenticationRepository.currentUser().execute(Dispatchers.IO) {
            Timber.i("Current collaborator is $it")
            copy(currentCollaboratorDocumentAsync = it)
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
                pagingData.map {
                    it.toClient()
                }
            }
        }
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

    private fun countTotalClients() {
        suspend {
            localClientRepository.totalClients()
        }.execute(Dispatchers.IO) {
            copy(totalClientsResponseAsync = it)
        }
    }

    private fun processTotalClientResponse(response: LocalClientTotalResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    totalClientsResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(totalClients = it)
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

    private suspend fun getPurchasesStream(): PurchaseListResponse = either {
        val query = PeyessQuery(
            queryFields = emptyList(),
            orderBy = listOf(
                PeyessOrderBy(
                    field = "created",
                    order = Order.DESCENDING,
                )
            ),
            groupBy = emptyList(),
        )

        purchaseRepository.paginatePurchases(query).map { pagingSourceFactory ->
            val pager = Pager(
                pagingSourceFactory = pagingSourceFactory,
                config = PagingConfig(
                    pageSize = purchasesTablePageSize,
                    enablePlaceholders = true,
                    prefetchDistance = lensesTablePrefetchDistance,
                ),
            )

            pager.flow.cancellable().cachedIn(viewModelScope)
        }.bind()
    }

    private fun processServiceOrderListResponse(response: PurchaseListResponse) = setState {
        response.fold(
            ifLeft = {
                copy(purchaseListResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(purchaseListStream = it)
            },
        )
    }

    private fun loadPurchases() {
        suspend {
            getPurchasesStream()
        }.execute(Dispatchers.IO) {
            copy(purchaseListResponseAsync = it)
        }
    }

    private fun processActiveSalesStreamResponse(
        response: ActiveSalesStreamResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(unfinishedSalesStreamAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(unfinishedSalesStream = it)
            }
        )
    }

    private fun processUnfinishedSaleStreamResponse(
        response: Flow<List<ServiceOrderDBView>>,
    ) {
        response.execute {
            copy(unfinishedSalesAsync = it)
        }
    }

    private fun updateUnfinishedSales(sales: List<ServiceOrderDBView>) = setState {
        copy(unfinishedSales = sales.map { it.toUnfinishedSale() })
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

    private fun streamUnfinishedSales() {
        authenticationRepository.currentUser()
            .filterNotNull()
            .map { saleRepository.unfinishedSalesStreamFor(it.id) }
            .execute(Dispatchers.IO) {
                copy(unfinishedSalesStreamAsync = it)
            }
    }

    private fun processCreateSaleResponse(response: CreateSaleResponse) = setState {
        response.fold(
            ifLeft = {
                copy(createSaleResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(hasCreatedSale = true)
            }
        )
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

    fun syncClients(context: Context) {
        enqueueOneTimeClientDownloadWorker(
            context = context,
            forceSync = true,
            workPolicy = ExistingWorkPolicy.REPLACE,
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

    fun startedUpdatingClient() = setState {
        copy(
            updateClient = false,
            updateClientId = "",
            cacheCreateClientInsertResponseAsync = Uninitialized,
            editClientResponseAsync = Uninitialized,
        )
    }

    fun loadEditClientToCache(clientId: String) {
        suspend {
            clientRepository.clientAndLegalById(clientId)
        }.execute {
            copy(editClientResponseAsync = it)
        }
    }

    suspend fun pictureForUser(uid: String): Uri {
        return collaboratorsRepository.pictureFor(uid)
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

    fun generateServiceOrderPdf(
        context: Context,
        purchase: PurchaseDocument,
        onPdfGenerationFailure: (err: Throwable) -> Unit = {},
        onPdfGenerated: (file: File) -> Unit = {}
    ) {
        suspend {
            val htmlToPdfConverter = HtmlToPdfConvertor(context)
            val html = purchase.legalText
            val file = createPrintFile(context)

            withContext(Dispatchers.Main) {
                htmlToPdfConverter.convert(
                    file,
                    html,
                    onPdfGenerationFailure,
                ) {
                    onPdfGenerated(it)
                }
            }
        }.execute(Dispatchers.IO) {
            val isGeneratingPdf = it is Loading

            copy(
                isGeneratingPdfFor = Pair(isGeneratingPdf, purchase.id),
            )
        }
    }


    fun startNewSale() = withState {
        suspend {
            saleRepository.deactivateSales()
            saleRepository.createSale()
        }.execute(Dispatchers.IO) {
            copy(createSaleResponseAsync = it)
        }
    }

    fun resumeSale(sale: UnfinishedSale) {
        suspend {
            saleRepository.resumeSale(sale.saleId, sale.serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(resumeSaleResponseAsync = it)
        }
    }

    fun cancelSale(sale: UnfinishedSale) {
        suspend {
            saleRepository.cancelSale(sale.saleId)
        }.execute(Dispatchers.IO) {
            copy(cancelSaleResponseAsync = it)
        }
    }

    fun newSaleStarted() = setState {
        copy(
            createSaleResponseAsync = Uninitialized,
            hasCreatedSale = false,
        )
    }

    fun exit() {
        viewModelScope.launch { authenticationRepository.exit() }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<MainViewModel, MainAppState> {
        override fun create(state: MainAppState): MainViewModel
    }

    companion object:
        MavericksViewModelFactory<MainViewModel, MainAppState> by hiltMavericksViewModelFactory()
}