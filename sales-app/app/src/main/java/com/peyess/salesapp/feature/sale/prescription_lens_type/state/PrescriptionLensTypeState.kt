package com.peyess.salesapp.feature.sale.prescription_lens_type.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.LensTypeCategoriesResponse

data class PrescriptionLensTypeState(
    val serviceOrderId: String = "",

    val currentServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrder: ActiveSOEntity = ActiveSOEntity(),

    val lensCategoriesResponseAsync: Async<LensTypeCategoriesResponse> = Uninitialized,
    val lensCategories: List<LensTypeCategoryDocument> = emptyList(),

    @PersistState
    val typeIdPicked: String = "",
    val typeCategoryPicked: LensTypeCategoryDocument? = null,

    val activeSO: Async<ActiveSOEntity> = Uninitialized,

    val hasUpdatedSale: Boolean = false,

    val mikeText: String = "",

    val goNextAttempts: Int = 0,
): MavericksState {
    val areCategoriesLoading = lensCategoriesResponseAsync is Loading
    val isMikeLoading = serviceOrderResponseAsync is Loading || mikeText.isBlank()

    val canGoNext = typeIdPicked.isNotEmpty()
}