package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.features.service_order_fetcher.ServiceOrderFetcher
import com.peyess.salesapp.screen.edit_service_order.adapter.toFrames
import com.peyess.salesapp.screen.edit_service_order.adapter.toPrescription
import com.peyess.salesapp.screen.edit_service_order.adapter.toServiceOrder
import com.peyess.salesapp.screen.sale.service_order.adapter.toColoring
import com.peyess.salesapp.screen.sale.service_order.adapter.toLens
import com.peyess.salesapp.screen.sale.service_order.adapter.toTreatment
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
    private val editProductPickedRepository: EditProductPickedRepository,
    private val editFramesDataRepository: EditFramesDataRepository,

    private val localLensesRepository: LocalLensesRepository,
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
                loadLensProducts(it.id)
                loadFrames(it.id)
            }
        }

        onEach(EditServiceOrderState::productPicked) {
            loadLens(it.lensId)
            loadColoring(it.coloringId)
            loadTreatment(it.treatmentId)
        }

        onAsync(EditServiceOrderState::serviceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(EditServiceOrderState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onAsync(EditServiceOrderState::productPickedResponseAsync) {
            processProductPickedResponse(it)
        }

        onAsync(EditServiceOrderState::lensResponseAsync) { processLensResponse(it) }
        onAsync(EditServiceOrderState::coloringResponseAsync) { processColoringResponse(it) }
        onAsync(EditServiceOrderState::treatmentResponseAsync) { processTreatmentResponse(it) }
        onAsync(EditServiceOrderState::framesResponseAsync) { processFramesResponse(it) }
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

    private fun loadLensProducts(serviceOrderId: String) {
        suspend {
            editProductPickedRepository.productPickedForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(productPickedResponseAsync = it)
        }
    }

    private fun processProductPickedResponse(
        response: EditProductPickedFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(productPickedResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(productPicked = it)
            },
        )
    }

    private fun loadLens(lensId: String) {
        suspend {
            localLensesRepository.getLensById(lensId)
        }.execute(Dispatchers.IO) {
            copy(lensResponseAsync = it)
        }
    }

    private fun processLensResponse(response: SingleLensResponse) = setState {
        response.fold(
            ifLeft = {
                copy(lensResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(lens = it.toLens())
            },
        )
    }

    private fun loadColoring(coloringId: String) {
        suspend {
            localLensesRepository.getColoringById(coloringId)
        }.execute(Dispatchers.IO) {
            copy(coloringResponseAsync = it)
        }
    }

    private fun processColoringResponse(response: SingleColoringResponse) = setState {
        response.fold(
            ifLeft = {
                copy(coloringResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(coloring = it.toColoring())
            },
        )
    }

    private fun loadTreatment(treatmentId: String) {
        suspend {
            localLensesRepository.getTreatmentById(treatmentId)
        }.execute(Dispatchers.IO) {
            copy(treatmentResponseAsync = it)
        }
    }

    private fun processTreatmentResponse(response: SingleTreatmentResponse) = setState {
        response.fold(
            ifLeft = {
                copy(treatmentResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(treatment = it.toTreatment())
            },
        )
    }

    private fun loadFrames(serviceOrderId: String) {
        suspend {
            editFramesDataRepository.findFramesForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(framesResponseAsync = it)
        }
    }

    private fun processFramesResponse(response: EditFramesFetchResponse) = setState {
        response.fold(
            ifLeft = {
                copy(framesResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(frames = it.toFrames())
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
