package com.peyess.salesapp.app.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryPagingError
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.repository.sale.ActiveSalesStreamResponse
import com.peyess.salesapp.repository.sale.CancelSaleResponse
import com.peyess.salesapp.repository.sale.ResumeSaleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

typealias ClientListStream = Flow<PagingData<Client>>
typealias ClientsListResponse = Either<LocalClientRepositoryPagingError, ClientListStream>

sealed class AppAuthenticationState {
    object Unauthenticated: AppAuthenticationState()
    object Authenticated: AppAuthenticationState()
    object Away: AppAuthenticationState()
}

data class MainAppState(
    val authState: AppAuthenticationState = AppAuthenticationState.Unauthenticated,

    val createNewSale: Async<Boolean> = Success(false),

    val productsTableStatusAsync: Async<ProductsTableStatus?> = Uninitialized,

    val serviceOrderListAsync: Async<List<ServiceOrderDocument>> = Uninitialized,

    val createClientResponseAsync: Async<CacheCreateClientCreateResponse> = Uninitialized,
    val createClientId: String = "",
    val createClient: Boolean = false,

    val existingCreateClientAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val existingCreateClientId: String = "",

    val clientListResponseAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListStream: ClientListStream = emptyFlow(),

    val currentCollaboratorDocumentAsync: Async<CollaboratorDocument?> = Uninitialized,
    val currentStoreAsync: Async<OpticalStore> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",

    val activeSalesStreamAsync: Async<ActiveSalesStreamResponse> = Uninitialized,
    val activeSalesAsync: Async<List<ActiveSalesEntity>> = Uninitialized,
    val activeSales: List<ActiveSalesEntity> = emptyList(),

    val cancelSaleResponseAsync: Async<CancelSaleResponse> = Uninitialized,
    val resumeSaleResponseAsync: Async<ResumeSaleResponse> = Uninitialized,

    val isGeneratingPdfFor: Pair<Boolean, String> = Pair(false, ""),
): MavericksState {
    val isCreatingNewSale = createNewSale is Loading

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

    val isSearchingForActiveSales = activeSalesStreamAsync is Loading
    val hasActiveSales = activeSales.isNotEmpty()

    val isServiceOrderListLoading = serviceOrderListAsync is Loading
    val serviceOrderList = serviceOrderListAsync.invoke() ?: emptyList()

    val isLookingForCreatingClient = existingCreateClientAsync is Loading
    val isCreatingClient = createClientResponseAsync is Loading
    val creatingClientExists = existingCreateClientId.isNotBlank()

    val areClientsLoading = clientListResponseAsync is Loading
}
