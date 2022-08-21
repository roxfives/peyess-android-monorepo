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

    private val lenses: Flow<PagingData<LensSuggestionModel>> =
        productRepository.filteredLenses(lensFilter = LensFilter())

    init {
        onEach(LensPickState::supplierLensFilter) {
            if (it.isEmpty()) {
                setState {
                    copy(
                        familyLensFilter = "",
                        descriptionLensFilter = "",
                        materialLensFilter = "",
                    )
                }
            }
        }
    }

    fun filteredLenses(): Flow<PagingData<LensSuggestionModel>> {
        return lenses
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensPickViewModel, LensPickState> {
        override fun create(state: LensPickState): LensPickViewModel
    }

    companion object: MavericksViewModelFactory<LensPickViewModel, LensPickState>
    by hiltMavericksViewModelFactory()
}