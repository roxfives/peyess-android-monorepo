package com.peyess.salesapp.app.state

import android.content.Context
import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.repository.sale.ActiveSalesStreamResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.file.createPrintFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.nvest.html_to_pdf.HtmlToPdfConvertor
import timber.log.Timber
import java.io.File

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val saleRepository: SaleRepository,
    private val clientRepository: ClientRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val purchaseRepository: PurchaseRepository,
    private val productsTableStateRepository: ProductsTableStateRepository,
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
                loadClients()
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

        onAsync(MainAppState::activeSalesStreamAsync) { processActiveSalesStreamResponse(it) }
        onAsync(MainAppState::activeSalesAsync) { processActiveSalesStream(it) }

        loadProductTableStatus()
        loadCurrentCollaborator()
        loadStore()
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

    private fun loadClients() = withState {
        clientRepository.clients()
            .execute {
                copy(clientListAsync = it)
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