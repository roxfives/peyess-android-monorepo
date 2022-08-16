package com.peyess.salesapp.feature.sale.prescription_lens_type.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import timber.log.Timber

class PrescriptionLensTypeViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionLensTypeState,
    val salesApplication: SalesApplication,
    private val salesRepository: SaleRepository,
) : MavericksViewModel<PrescriptionLensTypeState>(initialState) {

    init {
        loadActiveSo()
        loadDefaultMessage()

        loadTypes()
        updatePicked()

        onEach(PrescriptionLensTypeState::typeIdPicked) {
            updateMikeText()
            updatePicked()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadDefaultMessage() = withState {
        salesRepository
            .activeSO()
            .filterNotNull()
            .mapLatest {
                Timber.i("Found SO ${it}")

                salesApplication
                    .stringResource(R.string.mike_lens_type_default)
                    .format(it.clientName)

            }
            .take(1)
            .execute {
                copy(mikeText = it)
            }
    }

    private fun loadTypes() = withState {
        salesRepository.lensTypeCategories().execute {
            copy(lensCategories = it)
        }
    }

    private fun updateMikeText() = withState {
        var text: String?

        if (it.lensCategories is Success && it.typeIdPicked.isNotEmpty()) {
            text = it.lensCategories.invoke()
                .find {  l -> l.id == it.typeIdPicked }
                ?.explanation

            text = text ?: salesApplication
                .stringResource(R.string.mike_lens_type_default)

            setState {
                copy(
                    mikeText = Success(text.format(it.activeSO.invoke()?.clientName ?: "cliente")),
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

    private fun loadActiveSo() = withState {
        salesRepository.activeSO().filterNotNull().execute {
            copy(activeSO = it)
        }
    }

    fun onPick(name: String) = setState {
        val picked: LensTypeCategory?
        val activeSOEntity = this.activeSO.invoke()

        if (this.lensCategories is Success) {
            picked = this.lensCategories.invoke().find { it.name == name }

            if (activeSOEntity != null && picked != null) {
                salesRepository.updateSO(activeSOEntity.copy(
                    isLensTypeMono = picked.isMono,
                    lensTypeCategoryName = LensTypeCategoryName.fromName(picked.name)!!
                ))
            }

            copy(typeIdPicked = picked?.id ?: "")
        } else {
            copy(typeIdPicked = "")
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