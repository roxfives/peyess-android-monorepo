package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetcher
import com.peyess.salesapp.screen.edit_service_order.adapter.toPrescription
import com.peyess.salesapp.screen.edit_service_order.adapter.toServiceOrder
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
    private val editServiceOrderRepository: EditServiceOrderRepository,
    private val editPrescriptionRepository: EditPrescriptionRepository,
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

        onEach(EditServiceOrderState::successfullyFetchedServiceOrder) { success ->
            if (success) {
                loadServiceOrder()
            }
        }

        onEach(EditServiceOrderState::serviceOrder) {
            if (it.id.isNotBlank()) {
                loadPrescription(it.id)
            }
        }

        onAsync(EditServiceOrderState::serviceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(EditServiceOrderState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }
    }

    private fun fetchServiceOrder(serviceOrderId: String, purchaseId: String) {
        suspend {
            serviceOrderFetcher.fetchFullSale(serviceOrderId, purchaseId)
        }.execute(Dispatchers.IO) {
            copy(serviceOrderFetchResponseAsync = it)
        }
    }

    private fun loadServiceOrder() = withState {
        suspend {
            editServiceOrderRepository.serviceOrderById(it.serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(serviceOrderResponseAsync = it)
        }
    }

    private fun processServiceOrderResponse(
        response: EditServiceOrderFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(serviceOrderResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(serviceOrder = it.toServiceOrder())
            },
        )
    }

    private fun loadPrescription(serviceOrderId: String) {
        suspend {
            editPrescriptionRepository.prescriptionByServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(prescriptionResponseAsync = it)
        }
    }

    private fun processPrescriptionResponse(
        response: EditPrescriptionFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(prescriptionResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(prescription = it.toPrescription())
            },
        )
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
