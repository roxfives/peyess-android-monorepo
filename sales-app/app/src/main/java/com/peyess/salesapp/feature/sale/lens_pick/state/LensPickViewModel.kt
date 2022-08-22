package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.PagingData
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.repository.products.LensFilter
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class LensPickViewModel @AssistedInject constructor(
    @Assisted initialState: LensPickState,
    val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,
): MavericksViewModel<LensPickState>(initialState) {

    private var lenses: Flow<PagingData<LensSuggestionModel>> =
        productRepository.filteredLenses(lensFilter = LensFilter())

    init {
        loadLensGroups()

        onEach(LensPickState::groupLensFilterId) {
            lenses = productRepository.filteredLenses(lensFilter = LensFilter())
        }

        onEach(LensPickState::supplierLensFilter) {
            if (it.isEmpty()) {
                setState {
                    copy(
                        familyLensFilter = "",
                        descriptionLensFilter = "",
                        materialLensFilter = "",

                        familyLensFilterId = "",
                        descriptionLensFilterId = "",
                        materialLensFilterId = "",
                    )
                }
            }
        }
    }

    private fun loadLensGroups() = withState {
        productRepository.lensGroups().execute {
            copy(groupsFilter = it)
        }
    }

    fun filteredLenses(): Flow<PagingData<LensSuggestionModel>> {
        return lenses
    }

    fun onPickGroup(groupId: String, groupName: String) = setState {
        copy(
            groupLensFilterId = groupId,
            groupLensFilter = groupName,
        )
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensPickViewModel, LensPickState> {
        override fun create(state: LensPickState): LensPickViewModel
    }

    companion object: MavericksViewModelFactory<LensPickViewModel, LensPickState>
    by hiltMavericksViewModelFactory()
}