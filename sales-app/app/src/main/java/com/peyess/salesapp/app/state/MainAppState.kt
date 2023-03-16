package com.peyess.salesapp.app.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.dao.sale.active_so.db_view.ServiceOrderDBView
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientInsertResponse
import com.peyess.salesapp.data.repository.client.ClientAndLegalResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientTotalResponse
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryPagingError
import com.peyess.salesapp.data.repository.payment.error.PurchaseRepositoryPaginationError
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.repository.sale.ActiveSalesStreamResponse
import com.peyess.salesapp.repository.sale.CancelSaleResponse
import com.peyess.salesapp.repository.sale.CreateSaleResponse
import com.peyess.salesapp.repository.sale.ResumeSaleResponse
import com.peyess.salesapp.screen.home.model.UnfinishedSale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

typealias ClientListStream = Flow<PagingData<Client>>
typealias ClientsListResponse = Either<LocalClientRepositoryPagingError, ClientListStream>

typealias PurchaseStream = Flow<PagingData<PurchaseDocument>>
typealias PurchaseListResponse =
        Either<PurchaseRepositoryPaginationError, PurchaseStream>

sealed class AppAuthenticationState {
    object Unauthenticated: AppAuthenticationState()
    object Authenticated: AppAuthenticationState()
    object Away: AppAuthenticationState()
}

data class MainAppState(
    val authState: AppAuthenticationState = AppAuthenticationState.Unauthenticated,

    val createSaleResponseAsync: Async<CreateSaleResponse> = Uninitialized,
    val hasCreatedSale: Boolean = false,

    val productsTableStatusAsync: Async<ProductsTableStatus?> = Uninitialized,

    val purchaseListResponseAsync: Async<PurchaseListResponse> = Uninitialized,
    val purchaseListStream: PurchaseStream = emptyFlow(),

    val createClientResponseAsync: Async<CacheCreateClientCreateResponse> = Uninitialized,
    val createClientId: String = "",
    val createClient: Boolean = false,

    val updateClientId: String = "",
    val updateClient: Boolean = false,

    val existingCreateClientAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val existingCreateClient: CacheCreateClientDocument = CacheCreateClientDocument(),
    val hasLookedForExistingClient: Boolean = false,

    val totalClientsResponseAsync: Async<LocalClientTotalResponse> = Uninitialized,
    val totalClients: Int = 0,

    val clientListResponseAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListStream: ClientListStream = emptyFlow(),

    val isSearchActive: Boolean = false,
    val clientSearchQuery: String = "",
    val clientListSearchAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListSearchStream: ClientListStream = emptyFlow(),

    val currentCollaboratorDocumentAsync: Async<CollaboratorDocument?> = Uninitialized,
    val currentStoreAsync: Async<OpticalStore> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",

    val unfinishedSalesStreamAsync: Async<ActiveSalesStreamResponse> = Uninitialized,
    val unfinishedSalesStream: Flow<List<ServiceOrderDBView>> = emptyFlow(),
    val unfinishedSalesAsync: Async<List<ServiceOrderDBView>> = Uninitialized,
    val unfinishedSales: List<UnfinishedSale> = emptyList(),

    val cancelSaleResponseAsync: Async<CancelSaleResponse> = Uninitialized,
    val resumeSaleResponseAsync: Async<ResumeSaleResponse> = Uninitialized,

    val isGeneratingPdfFor: Pair<Boolean, String> = Pair(false, ""),

    val editClientResponseAsync: Async<ClientAndLegalResponse> = Uninitialized,
    val cacheCreateClientInsertResponseAsync: Async<CacheCreateClientInsertResponse> = Uninitialized,
): MavericksState {
    val isLoadingClientToEdit = editClientResponseAsync is Loading
            || cacheCreateClientInsertResponseAsync is Loading

    val isCreatingNewSale = createSaleResponseAsync is Loading

    val isUpdatingProducts = productsTableStatusAsync.invoke()?.isUpdating ?: false
    val hasProductUpdateFailed = productsTableStatusAsync.invoke()?.hasUpdateFailed ?: true

    // Home Screen
    val isLoadingCollaborator: Boolean = false
    val collaboratorDocument: CollaboratorDocument? = if (currentCollaboratorDocumentAsync !is Success) {
        CollaboratorDocument()
    } else {
        currentCollaboratorDocumentAsync.invoke()
    }

    val isLoadingStore: Boolean = false
    val store: OpticalStore = if (currentStoreAsync !is Success) {
        OpticalStore()
    } else {
        currentStoreAsync.invoke()
    }

    val isSearchingForActiveSales = unfinishedSalesStreamAsync is Loading
    val hasActiveSales = unfinishedSales.isNotEmpty()

    val isServiceOrderListLoading = purchaseListResponseAsync is Loading
//    val serviceOrderList = serviceOrderListAsync.invoke() ?: emptyList()

    val isLookingForCreatingClient = existingCreateClientAsync is Loading
    val isCreatingClient = createClientResponseAsync is Loading
    val existingCreateClientId = existingCreateClient.id
    val creatingClientExists = existingCreateClientId.isNotBlank()

    val areClientsLoading = clientListResponseAsync is Loading

    val isLoadingClientSearch = clientListSearchAsync is Loading
}
