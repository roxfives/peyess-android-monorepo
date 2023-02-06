package com.peyess.salesapp.screen.sale.pick_client.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.local_client.error.LocalClientRepositoryPagingError
import com.peyess.salesapp.screen.sale.pick_client.model.Client
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

typealias ClientListStream = Flow<PagingData<Client>>
typealias ClientsListResponse = Either<LocalClientRepositoryPagingError, ClientListStream>

data class PickClientState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val activeServiceOrderResponse: ActiveSOEntity = ActiveSOEntity(),

    val clientListResponseAsync: Async<ClientsListResponse> = Uninitialized,
    val clientListStream: ClientListStream = emptyFlow(),

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
): MavericksState {
    val saleId: String = activeServiceOrderResponse.saleId
    val serviceOrderId: String = activeServiceOrderResponse.id

    val isLoading = clientListResponseAsync is Loading

    val isLookingForCreatingClient = existingCreateClientAsync is Loading
    val isCreatingClient = createClientResponseAsync is Loading
    val existingCreateClientId = existingCreateClient.id
    val creatingClientExists = existingCreateClientId.isNotBlank()

    val areClientsLoading = clientListResponseAsync is Loading
}
