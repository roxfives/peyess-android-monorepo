package com.peyess.salesapp.app.state

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.app.adapter.toClient
import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.repository.sale.ActiveSalesStreamResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.file.createPrintFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.nvest.html_to_pdf.HtmlToPdfConvertor
import timber.log.Timber
import java.io.File

private const val clientsTablePageSize = 20
private const val lensesTablePrefetchDistance = 10

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    private val salesApplication: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val saleRepository: SaleRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val purchaseRepository: PurchaseRepository,
    private val productsTableStateRepository: ProductsTableStateRepository,
    private val localClientRepository: LocalClientRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
): MavericksViewModel<MainAppState>(initialState) {

    init {
        streamActiveSales()

        setState {
            copy(
                createNewSale = Success(false),
            )
        }

        onEach(MainAppState::authState) {
            if (it is AppAuthenticationState.Authenticated) {
                updateClientList()
                loadServiceOrders()
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

        onAsync(MainAppState::existingCreateClientAsync) { processActiveCreatingClientResponse(it) }
        onAsync(MainAppState::createClientResponseAsync) { processCreateNewClientResponse(it) }

        onAsync(MainAppState::clientListResponseAsync) { processClientListResponse(it) }

        onAsync(MainAppState::activeSalesStreamAsync) { processActiveSalesStreamResponse(it) }
        onAsync(MainAppState::activeSalesAsync) { processActiveSalesStream(it) }

        loadProductTableStatus()
        loadCurrentCollaborator()
        loadStore()
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
            Timber.i("Current colalborator is $it")
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

        return localClientRepository.paginateClients(query).map { pagingSourceFn ->

            val pager = Pager(
                pagingSourceFactory = pagingSourceFn,
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

    private fun updateClientList() {
        suspend {
            updatedClientListStream()
        }.execute(Dispatchers.IO) {
            copy(clientListResponseAsync = it)
        }
    }

    private fun loadServiceOrders() = withState {
        serviceOrderRepository
            .serviceOrders()
            .execute(Dispatchers.IO) {
                copy(serviceOrderListAsync = it)
            }

    }

    private fun processActiveSalesStreamResponse(response: ActiveSalesStreamResponse) {
        response.fold(
            ifLeft = {
                setState {
                    copy(activeSalesStreamAsync = Fail(it.error ?: Throwable(it.description)))
                }
            },

            ifRight = { stream ->
                stream.execute(Dispatchers.IO) {
                    copy(activeSalesAsync = it)
                }
            }
        )
    }

    private fun processActiveSalesStream(sales: List<ActiveSalesEntity>) = setState {
        copy(activeSales = sales)
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

    suspend fun pictureForUser(uid: String): Uri {
        return collaboratorsRepository.pictureFor(uid)
    }

    fun generateServiceOrderPdf(
        context: Context,
        serviceOrder: ServiceOrderDocument,
        onPdfGenerationFailure: (err: Throwable) -> Unit = {},
        onPdfGenerated: (file: File) -> Unit = {}
    ) = withState {
        suspend {
            purchaseRepository
                .getById(serviceOrder.purchaseId)
        }.execute(Dispatchers.IO) {
            if (it is Success && it.invoke() != null) {
                viewModelScope.launch(Dispatchers.Main) {
                    val htmlToPdfConverter = HtmlToPdfConvertor(context)

                    val purchase = it.invoke()
                    val html = purchase?.legalText

                    val file = createPrintFile(context)
                    if (html != null) {
                        htmlToPdfConverter.convert(
                            file,
                            html,
                            onPdfGenerationFailure,
                            { onPdfGenerated(it) },
                        )
                    }
                }
            }

            val isGeneratingPdf = it is Loading
            val generatingFor = if (isGeneratingPdf) serviceOrder.id else ""

            copy(
                isGeneratingPdfFor = Pair(isGeneratingPdf, generatingFor),
            )
        }
    }

    private fun streamActiveSales() {
        authenticationRepository.currentUser()
            .filterNotNull()
            .map { saleRepository.activeSalesStreamFor(it.id) }
            .execute(Dispatchers.IO) {
                copy(activeSalesStreamAsync = it)
            }
    }

    fun startNewSale() = withState {
        saleRepository.createSale().execute(Dispatchers.IO) {
            Timber.i("Creating a new sale $it")
            copy(createNewSale = it)
        }
    }

    fun resumeSale(sale: ActiveSalesEntity) {
        suspend {
            saleRepository.resumeSale(sale)
        }.execute(Dispatchers.IO) {
            copy(resumeSaleResponseAsync = it)
        }
    }

    fun cancelSale(sale: ActiveSalesEntity) {
        suspend {
            saleRepository.cancelSale(sale)
        }.execute(Dispatchers.IO) {
            copy(cancelSaleResponseAsync = it)
        }
    }

    fun newSaleStarted() = setState {
        copy(createNewSale = Success(false))
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