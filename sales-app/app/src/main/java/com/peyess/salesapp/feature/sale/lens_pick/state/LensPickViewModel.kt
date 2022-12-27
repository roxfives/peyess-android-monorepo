package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.Pager
import androidx.paging.PagingConfig
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
import com.peyess.salesapp.data.repository.lenses.room.SimplifiedQueryFields
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensGroupModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensDescriptionModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensFamilyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensMaterialModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSpecialtyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSupplierModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensTypeModel
import com.peyess.salesapp.feature.sale.lens_pick.constant.ListFilter
import com.peyess.salesapp.feature.sale.lens_pick.model.LensListFilter
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

    private var suggestionList: Flow<List<LensSuggestionModel?>> = emptyFlow()

    init {
        onEach(LensPickState::typeLensFilterId) { updateFilterForType(it) }
        onEach(LensPickState::supplierLensFilterId) { updateFilterForSupplier(it) }
        onEach(LensPickState::familyLensFilterId) { updateFilterForFamily(it) }
        onEach(LensPickState::descriptionLensFilterId) { updateFilterForDescription(it) }
        onEach(LensPickState::materialLensFilterId) { updateFilterForMaterial(it) }
        onEach(LensPickState::specialtyLensFilterId) { updateFilterForSpecialty(it) }
        onEach(LensPickState::groupLensFilterId) { updateFilterForGroup(it) }
        onEach(LensPickState::hasFilterUv) { updateFilterForUvLight(it) }
        onEach(LensPickState::hasFilterBlue) { updateFilterForBlueLight(it) }

        onAsync(LensPickState::lensesTypesResponseAsync) { processLensTypes(it) }
        onAsync(LensPickState::lensesSuppliersResponseAsync) { processLensSuppliers(it) }
        onAsync(LensPickState::lensesFamiliesResponseAsync) { processLensFamilies(it) }
        onAsync(LensPickState::lensesDescriptionsResponseAsync) { processLensDescriptions(it) }
        onAsync(LensPickState::lensesMaterialsResponseAsync) { processLensMaterials(it) }
        onAsync(LensPickState::lensesSpecialtiesResponseAsync) { processLensSpecialties(it) }
        onAsync(LensPickState::lensesGroupsResponseAsync) { processLensGroups(it) }

        onEach(LensPickState::filter) { updateLensTablePaging(it) }
        onAsync(LensPickState::lensesTableResponse) { processLensTableResponse(it) }

        onAsync(LensPickState::groupsList) { groupList ->
            viewModelScope.launch(Dispatchers.IO) {
                val lensesByGroup: MutableList<Flow<LensSuggestionModel?>> = mutableListOf()

                for (group in groupList) {
                    lensesByGroup.add(bestLensForGroup(group.id))
                }

                suggestionList = combine(lensesByGroup) { it.asList() }
            }
        }

        viewModelScope.launch(Dispatchers.IO) { updateLensTablePaging() }
    }

    private fun updateFilterForType(typeId: String) = setState {
        copy(filter = filter.copy(lensTypeId = typeId))
    }

    private fun updateFilterForSupplier(supplierId: String)  = withState { state ->
        if (state.filter.supplierId != supplierId) {
            setState {
                copy(
                    filter = filter.copy(
                        supplierId = supplierId,
                        familyId = "",
                        descriptionId = "",
                        materialId = "",
                        specialtyId = "",
                        groupId = "",
                    ),

                    familyLensFilter = "",
                    familyLensFilterId = "",

                    descriptionLensFilter = "",
                    descriptionLensFilterId = "",

                    materialLensFilter = "",
                    materialLensFilterId = "",

                    specialtyLensFilter = "",
                    specialtyLensFilterId = "",

                    groupLensFilter = "",
                    groupLensFilterId = "",
                )
            }
        }
    }

    private fun updateFilterForFamily(familyId: String) = withState { state ->
        if (state.filter.familyId != familyId) {
            setState {
                copy(
                    filter = filter.copy(
                        familyId = familyId,
                        descriptionId = "",
                        materialId = "",
                        specialtyId = "",
                        groupId = "",
                    ),

                    descriptionLensFilter = "",
                    descriptionLensFilterId = "",

                    materialLensFilter = "",
                    materialLensFilterId = "",

                    specialtyLensFilter = "",
                    specialtyLensFilterId = "",

                    groupLensFilter = "",
                    groupLensFilterId = "",
                )
            }
        }
    }

    private fun updateFilterForDescription(descriptionId: String) = withState { state ->
        if (state.filter.descriptionId != descriptionId) {
            setState {
                copy(
                    filter = filter.copy(
                        descriptionId = descriptionId,
                        materialId = "",
                        specialtyId = "",
                        groupId = "",
                    ),

                    materialLensFilter = "",
                    materialLensFilterId = "",

                    specialtyLensFilter = "",
                    specialtyLensFilterId = "",

                    groupLensFilter = "",
                    groupLensFilterId = "",
                )
            }
        }
    }

    private fun updateFilterForMaterial(materialId: String) = withState { state ->
        if (state.filter.materialId != materialId) {
            setState {
                copy(
                    filter = filter.copy(
                        materialId = materialId,
                        specialtyId = "",
                        groupId = "",
                    ),

                    specialtyLensFilter = "",
                    specialtyLensFilterId = "",

                    groupLensFilter = "",
                    groupLensFilterId = "",
                )
            }
        }
    }

    private fun updateFilterForSpecialty(specialtyId: String) = withState { state ->
        if (state.filter.specialtyId != specialtyId) {
            setState {
                copy(
                    filter = filter.copy(
                        specialtyId = specialtyId,
                        groupId = "",
                    ),

                    groupLensFilter = "",
                    groupLensFilterId = "",
                )
            }
        }
    }

    private fun updateFilterForGroup(groupId: String) = withState { state ->
        if (state.filter.groupId != groupId) {
            setState {
                copy(
                    filter = filter.copy(groupId = groupId)
                )
            }
        }
    }

    private fun updateFilterForUvLight(hasFilterUv: Boolean) = setState {
        copy(filter = filter.copy(withFilterUv = hasFilterUv))
    }

    private fun updateFilterForBlueLight(hasFilterBlue: Boolean) = setState {
        copy(filter = filter.copy(withFilterBlue = hasFilterBlue))
    }

    private fun shouldFilterByType(filter: ListFilter): Boolean {
        return false
    }

    private fun shouldFilterBySupplier(filter: ListFilter): Boolean {
        return filter == ListFilter.LensFamily
                || filter == ListFilter.LensDescription
                || filter == ListFilter.LensMaterial
                || filter == ListFilter.LensSpecialty
                || filter == ListFilter.LensGroup
    }

    private fun shouldFilterByFamily(filter: ListFilter): Boolean {
        return filter == ListFilter.LensDescription
                || filter == ListFilter.LensMaterial
                || filter == ListFilter.LensSpecialty
                || filter == ListFilter.LensGroup
    }

    private fun shouldFilterByDescription(filter: ListFilter): Boolean {
        return filter == ListFilter.LensMaterial
                || filter == ListFilter.LensSpecialty
                || filter == ListFilter.LensGroup
    }

    private fun shouldFilterByMaterial(filter: ListFilter): Boolean {
        return filter == ListFilter.LensSpecialty
                || filter == ListFilter.LensGroup
    }

    private fun shouldFilterBySpecialty(filter: ListFilter): Boolean {
        return filter == ListFilter.LensGroup
    }

    private fun buildFilterQueryFields(
        filter: ListFilter,
        activeListFilter: LensListFilter,
    ): List<PeyessQueryField> {
        val queryFields = mutableListOf<PeyessQueryField>()


        if (shouldFilterByType(filter) && activeListFilter.lensTypeId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensType.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.lensTypeId,
                )
            )
        }

        if (shouldFilterBySupplier(filter) && activeListFilter.supplierId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensSupplier.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.supplierId,
                )
            )
        }

        if (shouldFilterByFamily(filter) && activeListFilter.familyId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensFamily.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.familyId,
                )
            )
        }

        if (shouldFilterByDescription(filter) && activeListFilter.descriptionId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensDescription.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.descriptionId,
                )
            )
        }

        if (shouldFilterByMaterial(filter) && activeListFilter.materialId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensMaterial.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.materialId,
                )
            )
        }

        if (shouldFilterBySpecialty(filter) && activeListFilter.specialtyId.isNotEmpty()) {
            queryFields.add(
                buildQueryField(
                    field = SimplifiedQueryFields.LensSpecialty.name(),
                    op = PeyessQueryOperation.Equal,
                    value = activeListFilter.specialtyId,
                )
            )
        }

        return queryFields
    }

    private fun buildFilterQueryOrderBy(): List<PeyessOrderBy> {
        return listOf(
            PeyessOrderBy(
                field = SimplifiedQueryFields.Name.name(),
                order = Order.ASCENDING,
            )
        )
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

    private fun buildLensListQueryFields(filter: LensListFilter): List<PeyessQueryField> {
        val queryFields = mutableListOf<PeyessQueryField>()

        if (filter.lensTypeId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensTypeId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.lensTypeId,
                )
            )
        }

        if (filter.supplierId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensSupplierId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.supplierId,
                )
            )
        }

        if (filter.familyId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensFamilyId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.familyId,
                )
            )
        }

        if (filter.descriptionId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensDescriptionId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.descriptionId,
                )
            )
        }

        if (filter.materialId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensMaterialId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.materialId,
                )
            )
        }

        if (filter.specialtyId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensSpecialtyId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.specialtyId,
                )
            )
        }

        if (filter.groupId.isNotBlank()) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensGroupId.name(),
                    op = PeyessQueryOperation.Equal,
                    value = filter.groupId,
                )
            )
        }

        if (filter.withFilterBlue) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensBlueLightFilter.name(),
                    op = PeyessQueryOperation.Equal,
                    value = 1,
                )
            )
        }

        if (filter.withFilterUv) {
            queryFields.add(
                buildQueryField(
                    field = LocalLensesQueryFields.LensUVLightFilter.name(),
                    op = PeyessQueryOperation.Equal,
                    value = 1,
                )
            )
        }

        return queryFields
    }

    private fun buildLensListQueryOrderBy(): List<PeyessOrderBy> {
        return listOf(
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensSupplierPriority.name(),
                order = Order.ASCENDING,
            ),
            PeyessOrderBy(
                field = LocalLensesQueryFields.LensSupplier.name(),
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

    private suspend fun getUpdatedLensesTableStream(filter: LensListFilter): TableLensesResponse {
        val queryFields = buildLensListQueryFields(filter)
        val queryOrderBy = buildLensListQueryOrderBy()
        val query = PeyessQuery(
            queryFields = queryFields,
            orderBy = queryOrderBy,
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

    private fun processLensTableResponse(response: TableLensesResponse) {
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

    private fun updateLensTablePaging(filter: LensListFilter = LensListFilter()) {
        suspend {
            getUpdatedLensesTableStream(filter)
        }.execute(Dispatchers.IO) {
            copy(lensesTableResponse = it)
        }
    }

    private fun bestLensForGroup(groupId: String): Flow<LensSuggestionModel?> {
        return productRepository
            .bestLensInGroup(groupId)
            .flowOn(Dispatchers.IO)
    }

    fun loadLensesTypes() = withState {
        suspend {
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = emptyList(),
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredTypes(query)
                .map { it.map { type -> type.toLensTypeModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesTypesResponseAsync = it)
        }
    }

    fun loadLensSuppliers() = withState {
        suspend {
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = emptyList(),
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredSuppliers(query)
                .map { it.map { supplier -> supplier.toLensSupplierModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesSuppliersResponseAsync = it)
        }
    }

    fun loadLensFamilies() = withState {
        suspend {
            val queryFields = buildFilterQueryFields(
                filter = ListFilter.LensFamily,
                activeListFilter = it.filter,
            )
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = queryFields,
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredFamilies(query)
                .map { it.map { family -> family.toLensFamilyModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesFamiliesResponseAsync = it)
        }
    }

    fun loadLensDescriptions() = withState {
        suspend {
            val queryFields = buildFilterQueryFields(
                filter = ListFilter.LensDescription,
                activeListFilter = it.filter,
            )
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = queryFields,
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredDescriptions(query)
                .map { it.map { description -> description.toLensDescriptionModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesDescriptionsResponseAsync = it)
        }
    }

    fun loadLensMaterials() = withState {
        suspend {
            val queryFields = buildFilterQueryFields(
                filter = ListFilter.LensMaterial,
                activeListFilter = it.filter,
            )
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = queryFields,
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredMaterials(query)
                .map { it.map { material -> material.toLensMaterialModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesMaterialsResponseAsync = it)
        }
    }

    fun loadLensSpecialties() = withState {
        suspend {
            val queryFields = buildFilterQueryFields(
                filter = ListFilter.LensSpecialty,
                activeListFilter = it.filter,
            )
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = queryFields,
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredSpecialties(query)
                .map { it.map { specialty -> specialty.toLensSpecialtyModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesSpecialtiesResponseAsync = it)
        }
    }

    fun loadLensGroups() = withState {
        suspend {
            val queryFields = buildFilterQueryFields(
                filter = ListFilter.LensGroup,
                activeListFilter = it.filter,
            )
            val queryOrderBy = buildFilterQueryOrderBy()
            val query = PeyessQuery(
                queryFields = queryFields,
                orderBy = queryOrderBy,
            )

            lensesRepository
                .getFilteredGroups(query)
                .map { it.map { group -> group.toLensGroupModel() } }
        }.execute(Dispatchers.IO) {
            copy(lensesGroupsResponseAsync = it)
        }
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