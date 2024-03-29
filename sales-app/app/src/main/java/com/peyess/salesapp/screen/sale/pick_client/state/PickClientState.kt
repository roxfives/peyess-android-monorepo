package com.peyess.salesapp.screen.sale.pick_client.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientInsertResponse
import com.peyess.salesapp.data.repository.client.ClientAndLegalResponse
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedFetchAllResponse
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryPagingError
import com.peyess.salesapp.data.repository.local_sale.client_picked.AllClientsPickedResponse
import com.peyess.salesapp.feature.client_list.model.Client
import com.peyess.salesapp.navigation.client_list.PickScenario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

typealias ClientListStream = Flow<PagingData<Client>>
typealias ClientsListResponse = Either<LocalClientRepositoryPagingError, ClientListStream>

data class PickClientState(
    val saleId: String = "",
    val serviceOrderId: String = "",

    val updateClientId: String = "",
    val updateClient: Boolean = false,

    val clientListResponseAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListStream: ClientListStream = emptyFlow(),

    val isSearchActive: Boolean = false,
    val clientSearchQuery: String = "",
    val clientListSearchAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListSearchStream: ClientListStream = emptyFlow(),

    val editClientPickedHighlightListAsync: Async<EditClientPickedFetchAllResponse> = Uninitialized,
    val clientPickedHighlightListAsync: Async<AllClientsPickedResponse> = Uninitialized,
    val clientUidHighlightList: List<String> = emptyList(),
    val clientHighlightListAsync: Async<List<Client>> = Uninitialized,
    val clientHighlightList: List<Client> = emptyList(),

    val hasPickedClient: Boolean = false,

    val paymentId: Long = 0L,
    val pickScenario: PickScenario = PickScenario.ServiceOrder,
    val pickedId: String = "",

    val createClientResponseAsync: Async<CacheCreateClientCreateResponse> = Uninitialized,
    val createClientId: String = "",
    val createClient: Boolean = false,

    val existingCreateClientAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val existingCreateClient: CacheCreateClientDocument = CacheCreateClientDocument(),
    val hasLookedForExistingClient: Boolean = false,

    val editClientResponseAsync: Async<ClientAndLegalResponse> = Uninitialized,
    val cacheCreateClientInsertResponseAsync: Async<CacheCreateClientInsertResponse> = Uninitialized,
): MavericksState {
    val isLoading = clientListResponseAsync is Loading

    val isLookingForCreatingClient = existingCreateClientAsync is Loading
    val isCreatingClient = createClientResponseAsync is Loading
    val existingCreateClientId = existingCreateClient.id
    val creatingClientExists = existingCreateClientId.isNotBlank()

    val areClientsLoading = clientListResponseAsync is Loading

    val isLoadingClientSearch = clientListSearchAsync is Loading
}
