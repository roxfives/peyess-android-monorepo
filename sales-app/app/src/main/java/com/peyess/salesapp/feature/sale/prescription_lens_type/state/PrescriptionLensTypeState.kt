package com.peyess.salesapp.feature.sale.prescription_lens_type.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument

data class PrescriptionLensTypeState(
    @PersistState val typeIdPicked: String = "",
    val typeCategoryPicked: LensTypeCategoryDocument? = null,

    val activeSO: Async<ActiveSOEntity> = Uninitialized,

    val hasUpdatedSale: Boolean = false,

    val mikeText: Async<String> = Uninitialized,
    val lensCategories: Async<List<LensTypeCategoryDocument>> = Uninitialized,

    val goNextAttempts: Int = 0,
): MavericksState {
    val canGoNext = typeIdPicked.isNotEmpty()
}