package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.PagingData
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.products.firestore.lens_description.LensDescription
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.model.products.LensGroup
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.repository.products.LensFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class LensPickState(
    val lenses: Flow<PagingData<LensSuggestionModel>> = flowOf(),
    val filter: LensFilter = LensFilter(),

    val groupLensFilter: String = "",
    val typeLensFilter: String = "",
    val supplierLensFilter: String = "",
    val materialLensFilter: String = "",
    val familyLensFilter: String = "",
    val descriptionLensFilter: String = "",

    val groupLensFilterId: String = "",
    val typeLensFilterId: String = "",
    val supplierLensFilterId: String = "",
    val materialLensFilterId: String = "",
    val familyLensFilterId: String = "",
    val descriptionLensFilterId: String = "",

    val groupsFilter: Async<List<LensGroup>> = Uninitialized,
    val typesFilter: Async<List<FilterLensTypeEntity>> = Uninitialized,
    val supplierFilter: Async<List<FilterLensSupplierEntity>> = Uninitialized,
    val materialFilter: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    val familyFilter: Async<List<FilterLensFamilyEntity>> = Uninitialized,
    val descriptionFilter: Async<List<LensDescription>> = Uninitialized,

    val groupsList: Async<List<LensGroup>> = Uninitialized,

    val hasAddedToSuggestion: Boolean = false,
    val isAddingToSuggestion: Boolean = false,
): MavericksState {
    val isFamilyLensFilterEnabled = supplierLensFilter.isNotEmpty()
    val isDescriptionLensFilterEnabled = supplierLensFilter.isNotEmpty() && familyLensFilter.isNotEmpty()
    val isMaterialLensFilterEnabled = supplierLensFilter.isNotEmpty()
}
