package com.peyess.salesapp.screen.sale.prescription_lens_type.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.data.repository.lenses.room.LensesTypeCategoriesResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionNotFound
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.LensTypeCategoriesResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.screen.sale.prescription_lens_type.adapter.toLensTypeCategory
import com.peyess.salesapp.screen.sale.prescription_lens_type.model.LensTypeCategory
import com.peyess.salesapp.screen.sale.prescription_picture.adapter.toPrescriptionPicture
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PrescriptionLensTypeViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionLensTypeState,
    private val salesApplication: SalesApplication,
    private val salesRepository: SaleRepository,
    private val prescriptionRepository: LocalPrescriptionRepository,
    private val lensRepository: LocalLensesRepository,
) : MavericksViewModel<PrescriptionLensTypeState>(initialState) {

    init {
        loadLensTypeCategories()
        loadCurrentServiceOrder()

        onEach(PrescriptionLensTypeState::serviceOrderId) {
            streamActiveServiceOrder(it)
            streamPrescription(it)
        }

        onEach(PrescriptionLensTypeState::typeCategoryPicked) { updateMikeText(it) }

        onAsync(PrescriptionLensTypeState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onAsync(PrescriptionLensTypeState::currentServiceOrderResponseAsync) {
            processCurrentServiceOrderResponse(it)
        }
        onAsync(PrescriptionLensTypeState::serviceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }
        onAsync(PrescriptionLensTypeState::lensCategoriesResponseAsync) {
            processLensTypeCategoriesResponse(it)
        }
    }

    private fun loadCurrentServiceOrder() {
        suspend {
            salesRepository.currentServiceOrder()
        }.execute {
            copy(currentServiceOrderResponseAsync = it)
        }
    }

    private fun processCurrentServiceOrderResponse(
        response: ActiveServiceOrderResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    currentServiceOrderResponseAsync =
                        Fail(it.error ?: Throwable("Unknown error")),
                )
            },

            ifRight = { serviceOrder ->
                loadDefaultMikeMessage(serviceOrder.clientName)

                copy(serviceOrderId = serviceOrder.id)
            }
        )
    }

    private fun streamPrescription(serviceOrderId: String) = withState {
        prescriptionRepository
            .streamPrescriptionForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(prescriptionResponseAsync = it)
            }
    }

    private fun processPrescriptionResponse(response: LocalPrescriptionResponse) = setState {
        response.fold(
            ifLeft = {
                if (it is PrescriptionNotFound) {
                    createPrescription()
                    copy(prescriptionResponseAsync = Uninitialized)
                } else {
                    copy(
                        prescriptionResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        )
                    )
                }
            },

            ifRight = {
                val picked = lensCategories.find { c -> it.lensTypeCategoryId == c.id }
                    ?: LensTypeCategory()

                copy(
                    prescriptionResponse = it.toPrescriptionPicture(),
                    typeCategoryPicked = picked,
                    typeIdPicked = picked.id,
                )
            }
        )
    }

    private fun createPrescription() = withState {
        suspend {
            prescriptionRepository.createPrescriptionForServiceOrder(it.serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(createPrescriptionResponseAsync = it)
        }
    }

    private fun loadDefaultMikeMessage(clientName: String) = withState {
        if (it.mikeText.isBlank()) {
            val text = salesApplication
                .stringResource(R.string.mike_lens_type_default)
                .format(clientName)

            setState { copy(mikeText = text) }
        }
    }

    private fun updateMikeText(type: LensTypeCategory) = setState {
        // TODO: use string resource
        val clientName = serviceOrder.clientName.ifBlank { "cliente" }
        val text = type.explanation.format(clientName)

        copy(mikeText = text)
    }

    private fun streamActiveServiceOrder(serviceOrderId: String) {
        salesRepository.streamServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(serviceOrderResponseAsync = it)
            }
    }

    private fun processServiceOrderResponse(
        response: ActiveServiceOrderResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    serviceOrderResponseAsync = Fail(it.error ?: Throwable("Unknown error")),
                )
            },

            ifRight = { copy(serviceOrder = it) }
        )
    }

    private fun loadLensTypeCategories() {
        suspend {
            lensRepository.lensTypeCategories()
        }.execute {
            copy(lensCategoriesResponseAsync = it)
        }
    }

    private fun processLensTypeCategoriesResponse(
        response: LensesTypeCategoriesResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    lensCategoriesResponseAsync = Fail(it.error ?: Throwable("Unknown error")),
                )
            },

            ifRight = {
                copy(lensCategories = it.map { c -> c.toLensTypeCategory() })
            }
        )
    }

    fun onPick(name: String) = withState { state ->
        val picked = state.lensCategories.find { it.name == name }

        if (picked != null) {
            viewModelScope.launch(Dispatchers.IO) {
                prescriptionRepository.updateHasAddition(state.serviceOrderId, !picked.isMono)
                prescriptionRepository.updateLensTypeCategory(
                    serviceOrderId = state.serviceOrderId,
                    lensTypeCategoryId = picked.id,
                    lensTypeCategory = picked.category,
                )
            }
        }
    }

    fun onNext() = setState {
        copy(hasUpdatedSale = false)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PrescriptionLensTypeViewModel, PrescriptionLensTypeState> {
        override fun create(state: PrescriptionLensTypeState): PrescriptionLensTypeViewModel
    }

    companion object: MavericksViewModelFactory<PrescriptionLensTypeViewModel, PrescriptionLensTypeState>
        by hiltMavericksViewModelFactory()
}