package com.peyess.salesapp.feature.sale.lens_pick.state

import android.util.Log
import androidx.paging.PagingData
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.repository.products.LensFilter
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toImmutableList
import timber.log.Timber

class LensPickViewModel @AssistedInject constructor(
    @Assisted initialState: LensPickState,
    private val saleRepository: SaleRepository,
    private val lensComparisonDao: LensComparisonDao,
    private val productRepository: ProductRepository,
): MavericksViewModel<LensPickState>(initialState) {

    private var lenses: Flow<PagingData<LensSuggestionModel>> = emptyFlow()
    private var suggestionList: Flow<List<LensSuggestionModel?>> = emptyFlow()

    init {
        loadLensGroups()
        loadLensTypes()
        loadLensSuppliers()

        onEach(LensPickState::filter) { filter ->
            withState { state ->
                if (state.hasLoadedAllBasicFilters) {
                    lenses = productRepository.filteredLenses(lensFilter = filter)
                }
            }
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

                productRepository.lensDescription(familyId).execute(Dispatchers.IO) { descriptions ->
                    Timber.i("Got descriptions $descriptions")
                    copy(descriptionFilter = descriptions)
                }
            }
        }

        onEach(LensPickState::supplierLensFilterId) { supplierId ->
            setState {
                copy(
                    filter = filter.copy(
                        supplierId = supplierId,
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

            if (supplierId.isNotEmpty()) {
                productRepository.lensMaterial(supplierId).execute(Dispatchers.IO) { materials ->
                    copy(materialFilter = materials)
                }

                productRepository.lensFamily(supplierId).execute(Dispatchers.IO) { families ->
                    copy(familyFilter = families)
                }
            }
        }

        onAsync(LensPickState::groupsList) { groupList ->
            viewModelScope.launch(Dispatchers.IO) {
                val lensesByGroup: MutableList<Flow<LensSuggestionModel?>> = mutableListOf()

                for (group in groupList) {
                    lensesByGroup.add(bestLensForGroup(group.id))
                }

                suggestionList = combine(lensesByGroup) { it.asList() }
            }
        }
    }

    private fun loadLensGroups() = withState {
        productRepository.lensGroups().execute(Dispatchers.IO) {
            copy(
                groupsFilter = it,
                groupsList = it,
            )
        }
    }

    private fun loadLensTypes() = withState {
        productRepository.lensTypes().execute(Dispatchers.IO) {
            copy(typesFilter = it)
        }
    }

    private fun loadLensSuppliers() = withState {
        productRepository.lensSuppliers().execute(Dispatchers.IO) {
            copy(supplierFilter = it)
        }
    }

    private fun bestLensForGroup(groupId: String): Flow<LensSuggestionModel?> {
        return productRepository
            .bestLensInGroup(groupId)
            .flowOn(Dispatchers.IO)
    }

    fun filteredLenses(): Flow<PagingData<LensSuggestionModel>> {
        return lenses.flowOn(Dispatchers.IO)
    }

    fun suggestions(): Flow<List<LensSuggestionModel?>> {
        return suggestionList
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

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onPickLens(lensId: String) = withState {
        saleRepository
            .activeSO()
            .filterNotNull()
            .flatMapLatest {
                combine(
                    productRepository.lensById(lensId).map { it?.defaultTreatment ?: "" },
                    productRepository.coloringsForLens(lensId).map { if (it.isNotEmpty()) it[0].id else "" },
                ) { treatmentId, coloringId ->
                    LensComparisonEntity(
                        soId = it.id,
                        originalLensId = lensId,
                        originalTreatmentId = treatmentId,
                        originalColoringId = coloringId,

                        comparisonLensId = lensId,
                        comparisonTreatmentId = treatmentId,
                        comparisonColoringId = coloringId,
                    )
                }
            }
            .execute(Dispatchers.IO) {
                Timber.i("Loading adding to comparison: $it")

                if (it is Success) {
                    // TODO: Add through repository
                    lensComparisonDao.add(it.invoke())

                    Timber.i("Adding comparison of lens $lensId")

                    copy(
                        hasAddedToSuggestion = true
                    )
                } else {
                    copy(
                        isAddingToSuggestion = true
                    )
                }

            }
    }

    fun lensPicked() = setState {
        copy(
            hasAddedToSuggestion = false,
            isAddingToSuggestion = false,
        )
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensPickViewModel, LensPickState> {
        override fun create(state: LensPickState): LensPickViewModel
    }

    companion object: MavericksViewModelFactory<LensPickViewModel, LensPickState>
    by hiltMavericksViewModelFactory()
}