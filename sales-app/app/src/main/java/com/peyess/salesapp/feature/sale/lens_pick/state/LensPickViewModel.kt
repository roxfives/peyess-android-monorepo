package com.peyess.salesapp.feature.sale.lens_pick.state

import android.util.Log
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
import timber.log.Timber

class LensPickViewModel @AssistedInject constructor(
    @Assisted initialState: LensPickState,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,
): MavericksViewModel<LensPickState>(initialState) {

    private var lenses: Flow<PagingData<LensSuggestionModel>> =
        productRepository.filteredLenses(lensFilter = LensFilter())

    init {
        loadLensGroups()
        loadLensTypes()
        loadLensSuppliers()

        onEach(LensPickState::filter) {
            lenses = productRepository.filteredLenses(lensFilter = it)
        }

        onEach(LensPickState::groupLensFilterId) {
            setState { copy(filter = filter.copy(groupId = it)) }
        }

        onEach(LensPickState::typeLensFilterId) {
            setState { copy(filter = filter.copy(lensTypeId = it)) }
        }

        onEach(LensPickState::materialLensFilterId) {
            setState { copy(filter = filter.copy(materialId = it)) }
        }

        onEach(LensPickState::familyLensFilterId) {
            setState { copy(filter = filter.copy(familyId = it)) }
        }

        onEach(LensPickState::descriptionLensFilterId) {
            setState { copy(filter = filter.copy(descriptionId = it)) }
        }

        onEach(LensPickState::familyLensFilterId) { familyId ->
            withState {
                Timber.i("Getting descriptions")

                productRepository.lensDescription(familyId).execute { descriptions ->
                    Timber.i("Got descriptions $descriptions")
                    Log.i("SUPER_TAG","Got descriptions $descriptions")
                    copy(descriptionFilter = descriptions)
                }
            }
        }

        onEach(LensPickState::supplierLensFilterId) {
            setState {
                copy(
                    filter = filter.copy(
                        supplierId = it,
                        materialId = "",
                        familyId = "",
                        descriptionId = "",
                    ),

                    materialLensFilter = "",
                    materialLensFilterId = "",

                    familyLensFilter = "",
                    familyLensFilterId = "",

                    descriptionLensFilter = "",
                    descriptionLensFilterId = "",
                )
            }

            withState {  state ->
                if (it.isNotEmpty()) {
                    productRepository.lensMaterial(it).execute { materials ->
                        copy(materialFilter = materials)
                    }

                    productRepository.lensFamily(it).execute { families ->
                        Timber.i("Got families: $families")
                        copy(familyFilter = families)
                    }
                }
            }
        }
    }

    private fun loadLensGroups() = withState {
        productRepository.lensGroups().execute {
            copy(groupsFilter = it)
        }
    }

    private fun loadLensTypes() = withState {
        productRepository.lensTypes().execute {
            copy(typesFilter = it)
        }
    }

    private fun loadLensSuppliers() = withState {
        productRepository.lensSuppliers().execute {
            copy(supplierFilter = it)
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

    fun onPickType(typeId: String, typeName: String) = setState {
        copy(
            typeLensFilterId = typeId,
            typeLensFilter = typeName,
        )
    }

    fun onPickSupplier(supplierId: String, supplierName: String) = setState {
        copy(
            supplierLensFilterId = supplierId,
            supplierLensFilter = supplierName,
        )
    }

    fun onPickMaterial(materialId: String, materialName: String) = setState {
        copy(
            materialLensFilterId = materialId,
            materialLensFilter = materialName,
        )
    }

    fun onPickFamily(familyId: String, familyName: String) = setState {
        copy(
            familyLensFilterId = familyId,
            familyLensFilter = familyName,
        )
    }

    fun onPickDescription(descriptionId: String, descriptionName: String) = setState {
        copy(
            descriptionLensFilterId = descriptionId,
            descriptionLensFilter = descriptionName,
        )
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensPickViewModel, LensPickState> {
        override fun create(state: LensPickState): LensPickViewModel
    }

    companion object: MavericksViewModelFactory<LensPickViewModel, LensPickState>
    by hiltMavericksViewModelFactory()
}