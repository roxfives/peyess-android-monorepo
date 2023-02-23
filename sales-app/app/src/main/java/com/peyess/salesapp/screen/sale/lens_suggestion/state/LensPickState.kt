package com.peyess.salesapp.screen.sale.lens_suggestion.state

import androidx.paging.PagingData
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.feature.lens_suggestion.model.LensSuggestionModel
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensGroupDocument
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterGroupImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterDescriptionImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterFamilyImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterMaterialImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterSpecialtyImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterSupplierImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensPickModel
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilterTypeImpl
import com.peyess.salesapp.feature.lens_suggestion.model.LensListFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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

typealias LensesGroupsCompleteResponse =
        Either<LocalLensRepositoryException, List<StoreLensGroupDocument>>

typealias LensesSuggestionsResponse =
        List<Either<LocalLensRepositoryException, LensPickModel?>>

typealias LensComparisonResult =
        Either<LocalLensRepositoryException, LensComparisonDocument>

data class LensPickState(
    val isEditingParameter: Boolean = false,
    val serviceOrderId: String = "",
    val saleId: String = "",

    val filter: LensListFilter = LensListFilter(),
    val lenses: Flow<PagingData<LensSuggestionModel>> = emptyFlow(),

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

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,

    val groupsListAsync: Async<LensesGroupsCompleteResponse> = Uninitialized,
    val groupsList: List<StoreLensGroupDocument> = emptyList(),

    val lensSuggestionsResponseAsync: Async<LensesSuggestionsResponse> = Uninitialized,
    val lensSuggestionsResponse: List<LensPickModel?> = emptyList(),

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

    val lensComparisonResultAsync: Async<LensComparisonResult> = Uninitialized,
    val lensComparisonResult: LensComparisonDocument = LensComparisonDocument(),
): MavericksState {
    val isSale = serviceOrderId.isNotBlank() && saleId.isNotBlank()

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

    val isLensListStreamLoading = lensesTableResponse is Loading

    val isFamilyLensFilterEnabled = supplierLensFilter.isNotEmpty()
            && lensesSuppliersResponseAsync !is Loading
    val isDescriptionLensFilterEnabled = supplierLensFilter.isNotEmpty()
            && familyLensFilter.isNotEmpty()
            && lensesSuppliersResponseAsync !is Loading
            && lensesFamiliesResponseAsync !is Loading
    val isMaterialLensFilterEnabled = supplierLensFilter.isNotEmpty()
            && lensesSuppliersResponseAsync !is Loading

    val hasAddedToSuggestion = lensComparisonResultAsync is Success
            && lensComparisonResultAsync.invoke().isRight()
    val isAddingToSuggestion = lensComparisonResultAsync is Loading
    val addingToSuggestionFailed = lensComparisonResultAsync is Fail
}