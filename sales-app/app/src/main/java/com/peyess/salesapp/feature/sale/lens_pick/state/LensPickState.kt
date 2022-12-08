package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.PagingData
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.lens.description.LensDescriptionDocument
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.data.model.lens.groups.LensGroupDocument
import com.peyess.salesapp.repository.products.LensFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
    val typesFilter: Async<List<FilterLensTypeEntity>> = Uninitialized,
    val supplierFilter: Async<List<FilterLensSupplierEntity>> = Uninitialized,
    val materialFilter: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    val familyFilter: Async<List<FilterLensFamilyEntity>> = Uninitialized,
    val descriptionFilter: Async<List<LensDescriptionDocument>> = Uninitialized,

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,

    val groupsList: Async<List<LensGroupDocument>> = Uninitialized,

    val hasAddedToSuggestion: Boolean = false,
    val isAddingToSuggestion: Boolean = false,
): MavericksState {
    val isFamilyLensFilterEnabled = supplierLensFilter.isNotEmpty()
    val isDescriptionLensFilterEnabled = supplierLensFilter.isNotEmpty() && familyLensFilter.isNotEmpty()
    val isMaterialLensFilterEnabled = supplierLensFilter.isNotEmpty()

    val hasLoadedAllBasicFilters = groupsFilter is Success
            && typesFilter is Success
            && supplierFilter is Success
            && specialtyFilter is Success
}
