package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import arrow.core.Either
import arrow.core.continuations.either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesGroupsQueryFields
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.Unexpected
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_pick.adapter.buildPrescription
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toDisponibility
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensGroupModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensDescriptionModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensFamilyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensMaterialModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSpecialtyModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensSupplierModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensType
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toLensTypeModel
import com.peyess.salesapp.feature.sale.lens_pick.adapter.toStringResource
import com.peyess.salesapp.feature.sale.lens_pick.constant.ListFilter
import com.peyess.salesapp.feature.sale.lens_pick.model.LensListFilter
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildFilterQueryFields
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildFilterQueryOrderBy
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildGroupByForLensSuggestions
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildLensListQueryFields
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildLensListQueryOrderBy
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildOrderByForLensSuggestions
import com.peyess.salesapp.feature.sale.lens_pick.state.query.buildQueryFieldsForLensSuggestions
import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
import com.peyess.salesapp.features.disponibility.model.Disponibility
import com.peyess.salesapp.features.disponibility.model.Prescription
import com.peyess.salesapp.features.disponibility.supportsPrescription
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

private const val lensesTablePageSize = 20
private const val lensesTablePrefetchDistance = 5

private typealias BestLensResponse = Either<LocalLensRepositoryException, LensPickModel?>

class LensPickViewModel @AssistedInject constructor(
    @Assisted initialState: LensPickState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val lensComparisonDao: LensComparisonDao,
    private val productRepository: ProductRepository,
    private val lensesRepository: LocalLensesRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
    private val localMeasuringRepository: LocalMeasuringRepository,
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

        onEach(LensPickState::isSale, LensPickState::filter) { isSale, filter ->
            updateLensTablePaging(isSale, filter)
        }
        onAsync(LensPickState::lensesTableResponse) { processLensTableResponse(it) }

        onAsync(LensPickState::groupsListAsync) { processLensGroupListResponse(it) }
        onEach(LensPickState::groupsList) { updateLensSuggestionsWith(it) }

        onAsync(LensPickState::lensSuggestionsResponseAsync) { processLensSuggestionResponse(it) }

        viewModelScope.launch(Dispatchers.IO) {
            withState { updateLensTablePaging(it.isSale) }
        }
        viewModelScope.launch(Dispatchers.IO) { loadGroups() }
    }

    private fun loadGroups() = withState {
        suspend {
            val query = PeyessQuery(
                orderBy = listOf(
                    PeyessOrderBy(
                        field = LocalLensesGroupsQueryFields.Priority.name(),
                        order = Order.ASCENDING
                    ),
                )
            )

            lensesRepository.getFilteredGroups(query)
        }.execute(Dispatchers.IO) {
            copy(groupsListAsync = it)
        }
    }

    private fun processLensGroupListResponse(response: LensesGroupsCompleteResponse) {
        response.fold(
            ifLeft = {
                Timber.e(it.description, it.error)
                setState{ copy(groupsListAsync = Fail(error = it.error ?: Throwable())) }
            },
            ifRight = {
                setState{ copy(groupsList = it) }
            }
        )
    }

    private fun updateLensSuggestionsWith(list: List<StoreLensGroupDocument>) = withState {
        suspend {
            list.map { findBestLensForGroup(it.id) }
        }.execute {
            copy(lensSuggestionsResponseAsync = it)
        }
    }

    private fun processLensSuggestionResponse(response: LensesSuggestionsResponse) = setState {
        val lensSuggestions = mutableListOf<LensPickModel?>()

        response.forEach { eitherResponse ->
            eitherResponse.fold(
                ifLeft = {
                    Timber.e(it.description, it.error)

                    setState {
                        copy(lensSuggestionsResponseAsync = Fail(error = it.error ?: Throwable()))
                    }
                },
                ifRight = {
                    lensSuggestions.add(it)
                }
            )
        }

        copy(lensSuggestionsResponse = lensSuggestions)
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

    private fun checkLensDisponibilities(
        lens: StoreLensWithDetailsDocument,
        prescription: Prescription,
    ): List<ReasonUnsupported> {
        val reasonsUnsupported: MutableSet<ReasonUnsupported> = mutableSetOf()

        var disponibility: Disponibility
        var unsupportedList: List<ReasonUnsupported>
        for (disp in lens.disponibilities) {
            disponibility = disp.toDisponibility(
                height = lens.height,
                lensType = prescription.lensType,
                alternativeHeights = lens.altHeights,
            )

            unsupportedList = supportsPrescription(disponibility, prescription)
            if (unsupportedList.isEmpty()) {
                reasonsUnsupported.clear()
                break
            } else {
                reasonsUnsupported.addAll(unsupportedList)
            }
        }

        return reasonsUnsupported.toList()
    }

    private suspend fun getPrescriptionForFilter():
            Either<LocalLensRepositoryException, Prescription> = either {
        val activeServiceOrder = saleRepository
            .currentServiceOrder()
            .mapLeft { Unexpected() }
            .bind()

        val localPrescription = localPrescriptionRepository
            .getPrescriptionForServiceOrder(activeServiceOrder.id)
            .mapLeft { Unexpected() }
            .bind()

        val measuringLeft = localMeasuringRepository
            .measuringForServiceOrder(activeServiceOrder.id, Eye.Left)
            .mapLeft { Unexpected() }
            .bind()

        val measuringRight = localMeasuringRepository
            .measuringForServiceOrder(activeServiceOrder.id, Eye.Right)
            .mapLeft { Unexpected() }
            .bind()

        val prescription = buildPrescription(
            lensType = activeServiceOrder.lensTypeCategoryName.toLensType(),
            localPrescriptionDocument = localPrescription,
            localMeasuringLeft = measuringLeft,
            localMeasuringRight = measuringRight,
        )

        prescription
    }

    private suspend fun getUpdatedLensesTableStream(
        isSale: Boolean,
        filter: LensListFilter,
    ): TableLensesResponse = either {
        val queryFields = buildLensListQueryFields(filter)
        val queryOrderBy = buildLensListQueryOrderBy()
        val query = PeyessQuery(
            queryFields = queryFields,
            orderBy = queryOrderBy,
        )

        val prescription = if (isSale) {
            getPrescriptionForFilter().bind()
        } else {
            null
        }

        lensesRepository
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

                pager.flow.map { pagingData ->
                    pagingData.map { lens ->
                        val reasonsUnsupported = if (isSale && prescription != null) {
                            checkLensDisponibilities(
                                lens = lens,
                                prescription = prescription,
                            ).map { reason ->
                                reason.toStringResource(salesApplication = salesApplication)
                            }
                        } else {
                            emptyList()
                        }

                        lens.toLensPickModel(reasonsUnsupported)
                    }
                }
            }.bind()
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

    private fun updateLensTablePaging(
        isSale: Boolean,
        filter: LensListFilter = LensListFilter(),
    ) {
        suspend {
            getUpdatedLensesTableStream(isSale, filter)
        }.execute(Dispatchers.IO) {
            copy(lensesTableResponse = it)
        }
    }

    private suspend fun findBestLensForGroup(groupId: String): BestLensResponse = either {
        val activeServiceOrder = saleRepository
            .currentServiceOrder()
            .mapLeft { Unexpected("Failed while fetching the current service order") }
            .bind()

        val localPrescription = localPrescriptionRepository
            .getPrescriptionForServiceOrder(activeServiceOrder.id)
            .mapLeft {
                Unexpected(
                    "Failed while fetching prescription for SO ${activeServiceOrder.id}"
                )
            }.bind()

        val measuringLeft = localMeasuringRepository
            .measuringForServiceOrder(activeServiceOrder.id, Eye.Left)
            .mapLeft {
                Unexpected(
                    "Failed while fetching measuring for left eye " +
                            "for SO ${activeServiceOrder.id}"
                )
            }.bind()

        val measuringRight = localMeasuringRepository
            .measuringForServiceOrder(activeServiceOrder.id, Eye.Right)
            .mapLeft {
                Unexpected(
                    "Failed while fetching measuring for right eye " +
                            "for SO ${activeServiceOrder.id}"
                )
            }.bind()

        val queryFields = buildQueryFieldsForLensSuggestions(
            lensGroupId = groupId,
            lensType = activeServiceOrder.lensTypeCategoryName.toLensType(),
            prescription = localPrescription,
            measuringLeft = measuringLeft,
            measuringRight = measuringRight,
        )

        val orderBy = buildOrderByForLensSuggestions()
        val groupBy = buildGroupByForLensSuggestions()
        val query = PeyessQuery(
            queryFields = queryFields,
            orderBy = orderBy,
            groupBy = groupBy,
            withLimit = 1,
        )

        lensesRepository
            .getLensFilteredByDisponibility(query)
            .map { it?.toLensPickModel() }
            .bind()
    }

    fun loadLensTypes() = withState {
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

    fun updateIsEditing(isEditing: Boolean) = setState {
        copy(isEditingParameter = isEditing)
    }

    fun updateServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun updateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
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