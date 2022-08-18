package com.peyess.salesapp.feature.sale.prescription_lens_type.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.model.products.LensTypeCategory

data class PrescriptionLensTypeState(
    @PersistState val typeIdPicked: String = "",
    val typeCategoryPicked: LensTypeCategory? = null,

    val activeSO: Async<ActiveSOEntity> = Uninitialized,

    val hasUpdatedSale: Boolean = false,

    val mikeText: Async<String> = Uninitialized,
    val lensCategories: Async<List<LensTypeCategory>> = Uninitialized,

    val goNextAttempts: Int = 0,
): MavericksState {
    val canGoNext = typeIdPicked.isNotEmpty()
}