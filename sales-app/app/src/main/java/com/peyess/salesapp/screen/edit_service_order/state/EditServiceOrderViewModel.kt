package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers

private typealias EditServiceOrderFactory =
        AssistedViewModelFactory<EditServiceOrderViewModel, EditServiceOrderState>
private typealias EditServiceOrderViewModelFactory =
        MavericksViewModelFactory<EditServiceOrderViewModel, EditServiceOrderState>

class EditServiceOrderViewModel @AssistedInject constructor(
    @Assisted initialState: EditServiceOrderState,
    private val serviceOrderFetcher: ServiceOrderFetcher,
): MavericksViewModel<EditServiceOrderState>(initialState) {

    init {
        onEach(
            EditServiceOrderState::saleId,
            EditServiceOrderState::serviceOrderId,
        ) { purchaseId, serviceOrderId ->
            if (purchaseId.isNotBlank() && serviceOrderId.isNotBlank()) {
                fetchServiceOrder(serviceOrderId, purchaseId)
            }
        }
    }

    private fun fetchServiceOrder(serviceOrderId: String, purchaseId: String) {
        suspend {
            serviceOrderFetcher.fetchFullSale(serviceOrderId, purchaseId)
        }.execute(Dispatchers.IO) {
            copy(serviceOrderFetchResponse = it)
        }
    }

    fun onSaleIdChanged(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onServiceOrderIdChanged(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    @AssistedFactory
    interface Factory: EditServiceOrderFactory {
        override fun create(state: EditServiceOrderState): EditServiceOrderViewModel
    }

    companion object: EditServiceOrderViewModelFactory by hiltMavericksViewModelFactory()
}
