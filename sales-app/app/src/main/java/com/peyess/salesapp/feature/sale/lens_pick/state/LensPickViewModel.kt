package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import arrow.core.Either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

private const val lensesTablePageSize = 20
private const val lensesTablePrefetchDistance = 5

class LensPickViewModel @AssistedInject constructor(
    @Assisted initialState: LensPickState,
    private val saleRepository: SaleRepository,
    private val lensComparisonDao: LensComparisonDao,
    private val productRepository: ProductRepository,
    private val lensesRepository: LocalLensesRepository,
): MavericksViewModel<LensPickState>(initialState) {

    private var lenses: Flow<PagingData<LensSuggestionModel>> = emptyFlow()
    private var suggestionList: Flow<List<LensSuggestionModel?>> = emptyFlow()

    init {
        loadLensGroups()
        loadLensSpecialties()
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

        onEach(LensPickState::specialtyLensFilterId) {
            setState { copy(filter = filter.copy(specialtyId = it)) }
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

        onEach(LensPickState::hasFilterUv) {
            setState { copy(filter = filter.copy(hasFilterUv = it)) }
        }

        onEach(LensPickState::hasFilterBlue) {
            setState { copy(filter = filter.copy(hasFilterBlue = it)) }
        }

        onEach(LensPickState::familyLensFilterId) { familyId ->
            withState {
                productRepository.lensDescription(familyId).execute(Dispatchers.IO) { descriptions ->
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

        onAsync(LensPickState::lensesTableResponse) { response ->
            response.fold(
                ifLeft = {
                    setState {
                        copy(
                            lensesTableStream = emptyFlow(),
                            lensesTableResponse = Fail(Throwable()),
                        )
                    }
                },
                ifRight = {
                    setState {
                        copy(lensesTableStream = it)
                    }
                }
            )
        }

        withState {
            lenses = productRepository.filteredLenses(lensFilter = it.filter)
        }

        viewModelScope.launch(Dispatchers.IO) { updateLensTablePaging() }
    }

    private suspend fun getUpdatedLensesTableStream(): TableLensesResponse {
        return lensesRepository
            .paginateLensesWithDetailsOnly()
            .map { pagingSource ->
                val pagingSourceFactory = { pagingSource }

                val pager = Pager(
                    pagingSourceFactory = pagingSourceFactory,
                    config = PagingConfig(
                        pageSize = lensesTablePageSize,
                        enablePlaceholders = true,
                        prefetchDistance = lensesTablePrefetchDistance,
                    ),
                )

                pager.flow.map {
                    it.map { lens -> lens.toLensPickModel() }
                }
            }
    }

    private fun updateLensTablePaging() {
        suspend {
            getUpdatedLensesTableStream()
        }.execute(Dispatchers.IO) {
            copy(lensesTableResponse = it)
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

    private fun loadLensSpecialties() = withState {
        productRepository.lensSpecialties().execute(Dispatchers.IO) {
            copy(specialtyFilter = it)
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

    fun onPickSpecialty(specialtyId: String, specialtyName: String) = setState {
        copy(
            specialtyLensFilterId = specialtyId,
            specialtyLensFilter = specialtyName,
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

    fun onFilterUvChanged(isSelected: Boolean) = setState {
        copy(hasFilterUv = isSelected)
    }

    fun onFilterBlueChanged(isSelected: Boolean) = setState {
        copy(hasFilterBlue = isSelected)
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