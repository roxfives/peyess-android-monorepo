package com.peyess.salesapp.feature.sale.prescription_lens_type.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import timber.log.Timber

class PrescriptionLensTypeViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionLensTypeState,
    val salesApplication: SalesApplication,
    private val salesRepository: SaleRepository,
) : MavericksViewModel<PrescriptionLensTypeState>(initialState) {

    init {
        loadDefaultMessage()

        loadTypes()
        updatePicked()

        onEach(PrescriptionLensTypeState::typeIdPicked) {
            updateMikeText()
            updatePicked()
        }

        onEach(PrescriptionLensTypeState::typeIdPicked) {  }
    }

    private fun loadDefaultMessage() = withState {
        Timber.i("Calling mother fucker")

        salesRepository.activeSO().map {
            Timber.i("Found SO ${it}")

            salesApplication
                .stringResource(R.string.mike_lens_type_default)
                .format(it.clientName)

        }.execute {
            Timber.i("Updating mf with $it")
            copy(mikeText = it)
        }
    }

    private fun loadTypes() = withState {
        salesRepository.lensTypeCategories().execute {
            copy(lensCategories = it)
        }
    }

    private fun updateMikeText() = withState {
        val text: String?

        if (it.lensCategories is Success && it.typeIdPicked.isNotEmpty()) {
            text = it.lensCategories.invoke()
                .find {  l -> l.id == it.typeIdPicked }
                ?.explanation

            setState {
                copy(
                    mikeText = Success(text ?: salesApplication
                        .stringResource(R.string.mike_lens_type_default)),
                )
            }
        }
    }

    private fun updatePicked() = withState {
        val category: LensTypeCategory?

        if (it.lensCategories is Success && it.typeIdPicked.isNotEmpty()) {
            category = it.lensCategories.invoke()
                .find {  l -> l.id == it.typeIdPicked }

            setState {
                copy(
                    typeCategoryPicked = category
                )
            }
        }
    }

    fun onPick(name: String) = setState {
        val picked: LensTypeCategory?

        if (this.lensCategories is Success) {
            picked = this.lensCategories.invoke().find { it.name == name }

            copy(typeIdPicked = picked?.id ?: "")
        } else {
            copy(typeIdPicked = "")
        }
    }

    fun updateSale() = withState { state ->
        salesRepository.activeSO()
            .take(1)
            .map {
//                salesRepository
//                    .updateSO(it.copy(isLensTypeMono = state.typeCategoryPicked?.isMono ?: true))
            }
            .execute(Dispatchers.IO) {
                val attemptedNext = if (it is Success) {
                    this.goNextAttempts + 1
                } else {
                    this.goNextAttempts
                }

                Timber.i("Current attempts ${attemptedNext}]")

                copy(
                    hasUpdatedSale = true,
                    goNextAttempts = attemptedNext
                )
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