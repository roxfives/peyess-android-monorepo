package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesQueryFields
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensGroupModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensDescriptionModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensFamilyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensMaterialModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSpecialtyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSupplierModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensTypeModel
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

//            if (supplierId.isNotEmpty()) {
//                productRepository.lensMaterial(supplierId).execute(Dispatchers.IO) { materials ->
//                    copy(materialFilter = materials)
//                }
//
//                productRepository.lensFamily(supplierId).execute(Dispatchers.IO) { families ->
//                    copy(lensesFamiliesResponse = families)
//                }
//            }
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
                    Timber.e("Failed to load lenses table: ${it.description}", it.error)

                    setState {
                        copy(
                            lensesTableStream = emptyFlow(),
                            lensesTableResponse = Fail(
                                error = it.error ?: Throwable(it.description)
                            ),
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

        onAsync(LensPickState::lensesTypesResponseAsync) { processLensTypes(it) }
        onAsync(LensPickState::lensesSuppliersResponseAsync) { processLensSuppliers(it) }
        onAsync(LensPickState::lensesFamiliesResponseAsync) { processLensFamilies(it) }
        onAsync(LensPickState::lensesDescriptionsResponseAsync) { processLensDescriptions(it) }
        onAsync(LensPickState::lensesMaterialsResponseAsync) { processLensMaterials(it) }
        onAsync(LensPickState::lensesSpecialtiesResponseAsync) { processLensSpecialties(it) }
        onAsync(LensPickState::lensesGroupsResponseAsync) { processLensGroups(it) }

        withState {
            lenses = productRepository.filteredLenses(lensFilter = it.filter)
        }

        viewModelScope.launch(Dispatchers.IO) { updateLensTablePaging() }
    }

    private fun buildLensSearchOrderBy(): List<PeyessOrderBy> {
        return listOf(
            PeyessOrderBy(
                field = LocalLensesQueryFields.SupplierPriority.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.Supplier.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensTypePriority.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensType.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensMaterialPriority.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensMaterial.name(),
                order = Order.ASCENDING,
            ),
        )
    }

    private suspend fun getUpdatedLensesTableStream(): TableLensesResponse {
        val query = PeyessQuery(
            queryFields = emptyList(),
            orderBy = buildLensSearchOrderBy(),
        )

        return lensesRepository
            .paginateLensesWithDetailsOnly(query)
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

    private fun processLensTypes(response: LensesTypesResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses types: ${it.description}", it.error)

                copy(
                    lensesTypesResponse = emptyList(),
                    lensesTypesResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesTypesResponse = it) }
        )
    }

    private fun processLensSuppliers(response: LensesSuppliersResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses suppliers: ${it.description}", it.error)

                copy(
                    lensesSuppliersResponse = emptyList(),
                    lensesSuppliersResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesSuppliersResponse = it) }
        )
    }

    private fun processLensFamilies(response: LensesFamiliesResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses families: ${it.description}", it.error)

                copy(
                    lensesFamiliesResponse = emptyList(),
                    lensesFamiliesResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesFamiliesResponse = it) }
        )
    }

    private fun processLensDescriptions(response: LensesDescriptionsResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses descriptions: ${it.description}", it.error)

                copy(
                    lensesDescriptionsResponse = emptyList(),
                    lensesDescriptionsResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesDescriptionsResponse = it) }
        )
    }

    private fun processLensMaterials(response: LensesMaterialsResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses materials: ${it.description}", it.error)

                copy(
                    lensesMaterialsResponse = emptyList(),
                    lensesMaterialsResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesMaterialsResponse = it) }
        )
    }

    private fun processLensSpecialties(response: LensesSpecialtiesResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses specialties: ${it.description}", it.error)

                copy(
                    lensesSpecialtiesResponse = emptyList(),
                    lensesSpecialtiesResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesSpecialtiesResponse = it) }
        )
    }

    private fun processLensGroups(response: LensesGroupsResponse) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Failed to load lenses groups: ${it.description}", it.error)

                copy(
                    lensesGroupsResponse = emptyList(),
                    lensesGroupsResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },
            ifRight = { copy(lensesGroupsResponse = it) }
        )
    }

    fun loadLensesTypes() = withState {
        suspend {
            lensesRepository
                .getFilteredTypes()
                .map { it.map { type -> type.toLensTypeModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesTypesResponseAsync = it)
        }
    }

    fun loadLensSuppliers() = withState {
        suspend {
            lensesRepository
                .getFilteredSuppliers()
                .map { it.map { supplier -> supplier.toLensSupplierModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesSuppliersResponseAsync = it)
        }
    }

    fun loadLensFamilies() = withState {
        suspend {
            lensesRepository
                .getFilteredFamilies()
                .map { it.map { family -> family.toLensFamilyModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesFamiliesResponseAsync = it)
        }
    }

    fun loadLensDescriptions() = withState {
        suspend {
            lensesRepository
                .getFilteredDescriptions()
                .map { it.map { description -> description.toLensDescriptionModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesDescriptionsResponseAsync = it)
        }
    }

    fun loadLensMaterials() = withState {
        suspend {
            lensesRepository
                .getFilteredMaterials()
                .map { it.map { material -> material.toLensMaterialModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesMaterialsResponseAsync = it)
        }
    }

    fun loadLensSpecialties() = withState {
        suspend {
            lensesRepository
                .getFilteredSpecialties()
                .map { it.map { specialty -> specialty.toLensSpecialtyModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesSpecialtiesResponseAsync = it)
        }
    }

    fun loadLensGroups() = withState {
        suspend {
            lensesRepository
                .getFilteredGroups()
                .map { it.map { group -> group.toLensGroupModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesGroupsResponseAsync = it)
        }
    }

    private fun bestLensForGroup(groupId: String): Flow<LensSuggestionModel?> {
        return productRepository
            .bestLensInGroup(groupId)
            .flowOn(Dispatchers.IO)
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