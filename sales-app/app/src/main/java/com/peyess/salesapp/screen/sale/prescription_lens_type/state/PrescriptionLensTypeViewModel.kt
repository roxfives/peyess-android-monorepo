package com.peyess.salesapp.screen.sale.prescription_lens_type.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.LensTypeCategoriesResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers

class PrescriptionLensTypeViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionLensTypeState,
    val salesApplication: SalesApplication,
    private val salesRepository: SaleRepository,
) : MavericksViewModel<PrescriptionLensTypeState>(initialState) {

    init {
        loadLensTypeCategories()
        loadCurrentServiceOrder()

        onEach(PrescriptionLensTypeState::serviceOrderId) {
            streamActiveServiceOrder(it)
        }
        onEach(PrescriptionLensTypeState::typeCategoryPicked) {
            it?.let { updateMikeText(it) }
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

    private fun loadDefaultMikeMessage(clientName: String) = withState {
        if (it.mikeText.isBlank()) {
            val text = salesApplication
                .stringResource(R.string.mike_lens_type_default)
                .format(clientName)

            setState { copy(mikeText = text) }
        }
    }

    private fun updateMikeText(type: LensTypeCategoryDocument) = setState {
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

            ifRight = { serviceOrder ->
                val picked = lensCategories.find {
                    it.name.lowercase() == serviceOrder.lensTypeCategoryName.toName()
                }

                copy(
                    serviceOrder = serviceOrder,

                    typeIdPicked = picked?.id ?: "",
                    typeCategoryPicked = picked,
                )
            }
        )
    }

    private fun loadLensTypeCategories() {
        suspend {
            salesRepository.lensTypeCategories()
        }.execute {
            copy(lensCategoriesResponseAsync = it)
        }
    }

    private fun processLensTypeCategoriesResponse(
        response: LensTypeCategoriesResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    lensCategoriesResponseAsync = Fail(it.error ?: Throwable("Unknown error")),
                )
            },

            ifRight = { copy(lensCategories = it) }
        )
    }

    private fun onServiceOrderChanged(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun onPick(name: String) = withState { state ->
        val picked = state.lensCategories.find { it.name == name }

        if (picked != null) {
            salesRepository.updateSO(
                state.serviceOrder.copy(
                    isLensTypeMono = picked.isMono,
                    lensTypeCategoryName = LensTypeCategoryName.fromName(picked.name)!!
                )
            )
        }

//        copy(
//            typeIdPicked = picked?.id ?: "",
//            typeCategoryPicked = picked,
//        )
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