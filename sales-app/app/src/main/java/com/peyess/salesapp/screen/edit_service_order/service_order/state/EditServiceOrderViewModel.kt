package com.peyess.salesapp.screen.edit_service_order.service_order.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.ReadClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningFetchBothResponse
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
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
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toClient
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toFrames
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toPayment
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toPrescription
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toServiceOrder
import com.peyess.salesapp.screen.sale.service_order.adapter.toColoring
import com.peyess.salesapp.screen.sale.service_order.adapter.toLens
import com.peyess.salesapp.screen.sale.service_order.adapter.toTreatment
import com.peyess.salesapp.typing.sale.ClientRole
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    private val editLocalPaymentRepository: EditLocalPaymentRepository,
    private val editClientPickedRepository: EditClientPickedRepository,
    private val editPaymentDiscountRepository: EditPaymentDiscountRepository,
    private val editPaymentFeeRepository: EditPaymentFeeRepository,
    private val editPositioningRepository: EditPositioningRepository,

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
                loadUserPicked(it.id)
                loadResponsiblePicked(it.id)
                loadWitnessPicked(it.id)

                loadPrescription(it.id)
                streamPositionings(it.id)
                loadLensProducts(it.id)
                loadFrames(it.id)

                streamPayments(it.saleId)
                streamDiscount(it.saleId)
                streamFee(it.saleId)
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

        onAsync(EditServiceOrderState::userPickedResponseAsync) {
            processUserPickedResponse(it)
        }

        onAsync(EditServiceOrderState::responsiblePickedResponseAsync) {
            processResponsiblePickedResponse(it)
        }

        onAsync(EditServiceOrderState::witnessPickedResponseAsync) {
            processWitnessPickedResponse(it)
        }

        onAsync(EditServiceOrderState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onAsync(EditServiceOrderState::positioningsResponseAsync) {
            processPositioningsResponse(it)
        }

        onAsync(EditServiceOrderState::productPickedResponseAsync) {
            processProductPickedResponse(it)
        }

        onAsync(EditServiceOrderState::lensResponseAsync) { processLensResponse(it) }
        onAsync(EditServiceOrderState::coloringResponseAsync) { processColoringResponse(it) }
        onAsync(EditServiceOrderState::treatmentResponseAsync) { processTreatmentResponse(it) }
        onAsync(EditServiceOrderState::framesResponseAsync) { processFramesResponse(it) }
        onAsync(EditServiceOrderState::paymentsResponseAsync) { processPaymentsResponse(it) }
        onAsync(EditServiceOrderState::discountResponseAsync) { processDiscountResponse(it) }
        onAsync(EditServiceOrderState::feeResponseAsync) { processFeeResponse(it) }
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

    private fun loadUserPicked(serviceOrderId: String) {
        editClientPickedRepository
            .streamClientPickedForServiceOrder(serviceOrderId, ClientRole.User)
            .execute(Dispatchers.IO) {
                copy(userPickedResponseAsync = it)
            }
    }

    private fun processUserPickedResponse(
        response: EditClientPickedFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(userPickedResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(userPicked = it.toClient())
            },
        )
    }

    private fun loadResponsiblePicked(serviceOrderId: String) {
        editClientPickedRepository
            .streamClientPickedForServiceOrder(serviceOrderId, ClientRole.Responsible)
            .execute(Dispatchers.IO) {
                copy(responsiblePickedResponseAsync = it)
            }
    }

    private fun processResponsiblePickedResponse(
        response: EditClientPickedFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(responsiblePickedResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(responsiblePicked = it.toClient())
            },
        )
    }

    private fun loadWitnessPicked(serviceOrderId: String) {
        editClientPickedRepository
            .streamClientPickedForServiceOrder(serviceOrderId, ClientRole.Witness)
            .execute(Dispatchers.IO) {
                copy(witnessPickedResponseAsync = it)
            }
    }

    private fun processWitnessPickedResponse(
        response: EditClientPickedFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                when (it) {
                    is ReadClientPickedError.ClientPickedNotFound ->
                        copy(hasWitness = false)
                    is ReadClientPickedError.Unexpected ->
                        copy(witnessPickedResponseAsync = Fail(it.error))
                }
            },

            ifRight = {
                copy(witnessPicked = it.toClient())
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

    private fun streamPositionings(serviceOrderId: String) {
        editPositioningRepository
            .streamBothPositioningForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(positioningsResponseAsync = it)
            }
    }

    private fun processPositioningsResponse(
        response: EditPositioningFetchBothResponse,
    ) = setState {
        response.fold(
            ifLeft = {
              copy(positioningsResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(positioningPair = it)
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
                copy(coloringResponse = it.toColoring())
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
                copy(treatmentResponse = it.toTreatment())
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

    private fun streamPayments(saleId: String) {
        editLocalPaymentRepository.streamPaymentsForSale(saleId)
            .execute(Dispatchers.IO) {
                copy(paymentsResponseAsync = it)
            }
    }

    private fun processPaymentsResponse(
        response: EditLocalPaymentFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(paymentsResponseAsync = Fail(it.error))
            },

            ifRight = {
                val totalPaid = if (it.isEmpty()) {
                    0.0
                } else {
                    it.map { p -> p.value }
                        .reduce { acc, value -> acc + value }
                }

                copy(
                    payments = it.map { p -> p.toPayment() },
                    totalPaid = totalPaid,
                )
            },
        )
    }

    private fun streamDiscount(saleId: String) {
        editPaymentDiscountRepository.streamDiscountForSale(saleId)
            .execute(Dispatchers.IO) {
                copy(discountResponseAsync = it)
            }
    }

    private fun processDiscountResponse(
        response: EditPaymentDiscountFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(discountResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(discount = it)
            },
        )
    }

    private fun streamFee(saleId: String) {
        editPaymentFeeRepository.streamPaymentFeeForSale(saleId)
            .execute(Dispatchers.IO) {
                copy(feeResponseAsync = it)
            }
    }

    private fun processFeeResponse(
        response: EditPaymentFeeFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(feeResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(fee = it)
            },
        )
    }

    fun onSaleIdChanged(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onServiceOrderIdChanged(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun deletePayment(paymentId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            editLocalPaymentRepository.deletePayment(paymentId)
        }
    }

    @AssistedFactory
    interface Factory: EditServiceOrderFactory {
        override fun create(state: EditServiceOrderState): EditServiceOrderViewModel
    }

    companion object: EditServiceOrderViewModelFactory by hiltMavericksViewModelFactory()
}