package com.peyess.salesapp.app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.navigation.pick_client.PickScenario

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

    // Client screen
    val clientListAsync: Async<List<ClientDocument>> = Uninitialized,

    val currentCollaboratorDocumentAsync: Async<CollaboratorDocument?> = Uninitialized,
    val currentStoreAsync: Async<OpticalStore> = Uninitialized,

    val hasPickedClient: Boolean = false,

    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",

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

    // Client screen
    val clientList = clientListAsync.invoke() ?: emptyList()

    val isServiceOrderListLoading = serviceOrderListAsync is Loading
    val serviceOrderList = serviceOrderListAsync.invoke() ?: emptyList()

    val areClientsLoading = clientListAsync is Loading || clientListAsync is Uninitialized
}
