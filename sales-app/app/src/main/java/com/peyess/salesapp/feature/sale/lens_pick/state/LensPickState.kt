package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.lens.description.LensDescriptionDocument
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.data.model.lens.groups.LensGroupDocument
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterGroupImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterDescriptionImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterFamilyImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterMaterialImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterSpecialtyImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterSupplierImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterTypeImpl
import com.peyess.salesapp.repository.products.LensFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

typealias TableLensesResponse =
        Either<LocalLensRepositoryException, Flow<PagingData<LensPickModel>>>

typealias LensesTypesResponse =
        Either<LocalLensRepositoryException, List<LensFilterTypeImpl>>

typealias LensesSuppliersResponse =
        Either<LocalLensRepositoryException, List<LensFilterSupplierImpl>>

typealias LensesFamiliesResponse =
        Either<LocalLensRepositoryException, List<LensFilterFamilyImpl>>

typealias LensesDescriptionsResponse =
        Either<LocalLensRepositoryException, List<LensFilterDescriptionImpl>>

typealias LensesMaterialsResponse =
        Either<LocalLensRepositoryException, List<LensFilterMaterialImpl>>

typealias LensesSpecialtiesResponse =
        Either<LocalLensRepositoryException, List<LensFilterSpecialtyImpl>>

typealias LensesGroupsResponse =
        Either<LocalLensRepositoryException, List<LensFilterGroupImpl>>

data class LensPickState(
    val lenses: Flow<PagingData<LensSuggestionModel>> = flowOf(),
    val filter: LensFilter = LensFilter(),

    val groupLensFilter: String = "",
    val specialtyLensFilter: String = "",
    val typeLensFilter: String = "",
    val supplierLensFilter: String = "",
    val materialLensFilter: String = "",
    val familyLensFilter: String = "",
    val descriptionLensFilter: String = "",

    val groupLensFilterId: String = "",
    val specialtyLensFilterId: String = "",
    val typeLensFilterId: String = "",
    val supplierLensFilterId: String = "",
    val materialLensFilterId: String = "",
    val familyLensFilterId: String = "",
    val descriptionLensFilterId: String = "",

    val groupsFilter: Async<List<LensGroupDocument>> = Uninitialized,
    val specialtyFilter: Async<List<FilterLensSpecialtyEntity>> = Uninitialized,
    val materialFilter: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    val descriptionFilter: Async<List<LensDescriptionDocument>> = Uninitialized,

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,

    val groupsList: Async<List<LensGroupDocument>> = Uninitialized,

    val hasAddedToSuggestion: Boolean = false,
    val isAddingToSuggestion: Boolean = false,

    val lensesTableResponse: Async<TableLensesResponse> = Uninitialized,
    val lensesTableStream: Flow<PagingData<LensPickModel>> = emptyFlow(),

    val lensesTypesResponseAsync: Async<LensesTypesResponse> = Uninitialized,
    val lensesTypesResponse: List<LensFilterTypeImpl> = emptyList(),

    val lensesSuppliersResponseAsync: Async<LensesSuppliersResponse> = Uninitialized,
    val lensesSuppliersResponse: List<LensFilterSupplierImpl> = emptyList(),

    val lensesFamiliesResponseAsync: Async<LensesFamiliesResponse> = Uninitialized,
    val lensesFamiliesResponse: List<LensFilterFamilyImpl> = emptyList(),

    val lensesDescriptionsResponseAsync: Async<LensesDescriptionsResponse> = Uninitialized,
    val lensesDescriptionsResponse: List<LensFilterDescriptionImpl> = emptyList(),

    val lensesMaterialsResponseAsync: Async<LensesMaterialsResponse> = Uninitialized,
    val lensesMaterialsResponse: List<LensFilterMaterialImpl> = emptyList(),

    val lensesSpecialtiesResponseAsync: Async<LensesSpecialtiesResponse> = Uninitialized,
    val lensesSpecialtiesResponse: List<LensFilterSpecialtyImpl> = emptyList(),

    val lensesGroupsResponseAsync: Async<LensesGroupsResponse> = Uninitialized,
    val lensesGroupsResponse: List<LensFilterGroupImpl> = emptyList(),
): MavericksState {
    val areTypesLoading = lensesTypesResponseAsync is Loading
    val hasTypesLoadingFailed = lensesTypesResponseAsync is Fail

    val areSuppliersLoading = lensesSuppliersResponseAsync is Loading
    val hasSuppliersLoadingFailed = lensesSuppliersResponseAsync is Fail

    val areFamiliesLoading = lensesFamiliesResponseAsync is Loading
    val hasFamiliesLoadingFailed = lensesFamiliesResponseAsync is Fail

    val areDescriptionsLoading = lensesDescriptionsResponseAsync is Loading
    val hasDescriptionsLoadingFailed = lensesDescriptionsResponseAsync is Fail

    val areMaterialsLoading = lensesMaterialsResponseAsync is Loading
    val hasMaterialsLoadingFailed = lensesMaterialsResponseAsync is Fail

    val areSpecialtiesLoading = lensesSpecialtiesResponseAsync is Loading
    val hasSpecialtiesLoadingFailed = lensesSpecialtiesResponseAsync is Fail

    val areGroupsLoading = lensesGroupsResponseAsync is Loading
    val hasGroupsLoadingFailed = lensesGroupsResponseAsync is Fail

    val isFamilyLensFilterEnabled = supplierLensFilter.isNotEmpty()
    val isDescriptionLensFilterEnabled = supplierLensFilter.isNotEmpty() && familyLensFilter.isNotEmpty()
    val isMaterialLensFilterEnabled = supplierLensFilter.isNotEmpty()

    val hasLoadedAllBasicFilters = groupsFilter is Success
            && lensesTypesResponseAsync is Success
            && lensesSuppliersResponseAsync is Success
            && specialtyFilter is Success
}