package com.peyess.salesapp.app.state

import android.content.Context
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.features.pdf.service_order.buildHtml
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.file.createPrintFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nvest.html_to_pdf.HtmlToPdfConvertor
import timber.log.Timber
import java.io.File

class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    private val authenticationRepository: AuthenticationRepository,
    private val saleRepository: SaleRepository,
    private val clientRepository: ClientRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val purchaseRepository: PurchaseRepository,
    private val productsTableStateRepository: ProductsTableStateRepository,
): MavericksViewModel<MainAppState>(initialState) {

    init {
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
                    val html = buildHtml(
                        context,
                        serviceOrder,
                        purchase!!,
                    )

                    val file = createPrintFile(context)
                    htmlToPdfConverter.convert(
                        file,
                        html,
                        onPdfGenerationFailure,
                        { onPdfGenerated(it) },
                    )
                }
            }

            val isGeneratingPdf = it is Loading
            val generatingFor = if (isGeneratingPdf) serviceOrder.id else ""

            copy(
                isGeneratingPdfFor = Pair(isGeneratingPdf, generatingFor),
            )
        }

    }

    fun startNewSale() = withState {
        saleRepository.createSale().execute(Dispatchers.IO) {
            Timber.i("Creating a new sale $it")
            copy(createNewSale = it)
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