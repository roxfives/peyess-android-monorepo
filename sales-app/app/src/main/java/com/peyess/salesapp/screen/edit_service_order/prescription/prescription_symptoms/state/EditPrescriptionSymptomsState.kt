package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

data class EditPrescriptionSymptomsState(
    val saleId: String = "",
    val serviceOrderId: String = "",
    val prescriptionId: String = "",

    val serviceOrderResponseAsync: Async<EditServiceOrderFetchResponse> = Uninitialized,
    val serviceOrderResponse: LocalServiceOrderDocument = LocalServiceOrderDocument(),

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescriptionResponse: LocalPrescriptionDocument = LocalPrescriptionDocument(),

    val mikeMessageAmetropies: String = "",
): MavericksState {
    val clientName = serviceOrderResponse.clientName
    val lensTypeCategoryName = prescriptionResponse.lensTypeCategory

    val sphericalLeft = prescriptionResponse.sphericalLeft
    val sphericalRight = prescriptionResponse.sphericalRight
    val cylindricalLeft = prescriptionResponse.cylindricalLeft
    val cylindricalRight = prescriptionResponse.cylindricalRight

    val hasHypermetropiaLeft =  sphericalLeft > 0
    val hasMyopiaLeft = sphericalLeft < 0
    val hasAstigmatismLeft = cylindricalLeft < 0
    val hasPresbyopiaLeft = lensTypeCategoryName == LensTypeCategoryName.Near
            || lensTypeCategoryName == LensTypeCategoryName.Multi

    val hasHypermetropiaRight =  sphericalRight > 0
            && lensTypeCategoryName == LensTypeCategoryName.Near
    val hasMyopiaRight = sphericalRight < 0
    val hasAstigmatismRight = cylindricalRight < 0;
    val hasPresbyopiaRight = lensTypeCategoryName == LensTypeCategoryName.Near
            || lensTypeCategoryName == LensTypeCategoryName.Multi
}
