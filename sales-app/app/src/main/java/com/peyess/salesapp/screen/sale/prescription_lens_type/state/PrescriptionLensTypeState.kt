package com.peyess.salesapp.screen.sale.prescription_lens_type.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.repository.lenses.room.LensesTypeCategoriesResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionInsertResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.feature.prescription.model.PrescriptionPicture
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.LensTypeCategoriesResponse
import com.peyess.salesapp.screen.sale.prescription_lens_type.model.LensTypeCategory

data class PrescriptionLensTypeState(
    val serviceOrderId: String = "",

    val currentServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrder: ActiveSOEntity = ActiveSOEntity(),

    val createPrescriptionResponseAsync: Async<LocalPrescriptionInsertResponse> = Uninitialized,
    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: PrescriptionPicture = PrescriptionPicture(),

    val lensCategoriesResponseAsync: Async<LensesTypeCategoriesResponse> = Uninitialized,
    val lensCategories: List<LensTypeCategory> = emptyList(),

    @PersistState
    val typeIdPicked: String = "",
    val typeCategoryPicked: LensTypeCategory = LensTypeCategory(),

    val activeSO: Async<ActiveSOEntity> = Uninitialized,

    val hasUpdatedSale: Boolean = false,

    val mikeText: String = "",

    val goNextAttempts: Int = 0,
): MavericksState {
    val areCategoriesLoading = lensCategoriesResponseAsync is Loading
    val isMikeLoading = serviceOrderResponseAsync is Loading || mikeText.isBlank()

    val canGoNext = typeIdPicked.isNotEmpty()
}