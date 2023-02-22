package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.feature.prescription.model.PrescriptionData
import com.peyess.salesapp.feature.prescription.utils.animationFor
import com.peyess.salesapp.feature.prescription.utils.messageFor
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.prescription.PrismPosition

data class EditPrescriptionDataState(
    val saleId: String = "",
    val serviceOrderId: String = "",
    val prescriptionId: String = "",

    val serviceOrderResponseAsync: Async<EditServiceOrderFetchResponse> = Uninitialized,
    val serviceOrderResponse: LocalServiceOrderDocument = LocalServiceOrderDocument(),

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescriptionResponse: PrescriptionData = PrescriptionData(),

    val mikeMessageAmetropies: String = "",
    val generalMessage: String = "",
): MavericksState {
    val isLoading = prescriptionResponseAsync is Loading

    val hasServiceOrderLoaded = serviceOrderResponseAsync is Success
            && serviceOrderResponseAsync.invoke().isRight()
    val lensTypeCategoryName = serviceOrderResponse.lensTypeCategoryName
    val clientName = serviceOrderResponse.clientName

    val isAnimationLoading = serviceOrderResponseAsync is Loading
    val animationId = animationFor(lensTypeCategoryName)

    val isMessageLoading = serviceOrderResponseAsync is Loading

    val hasAddition = !serviceOrderResponse.isLensTypeMono
    val hasPrism = prescriptionResponse.hasPrism

    val hasAxisLeft = prescriptionResponse.cylindricalLeft < 0.0
    val hasAxisRight = prescriptionResponse.cylindricalRight < 0.0

    val sphericalLeft = prescriptionResponse.sphericalLeft
    val sphericalRight = prescriptionResponse.sphericalRight
    val cylindricalLeft = prescriptionResponse.cylindricalLeft
    val cylindricalRight = prescriptionResponse.cylindricalRight
    val axisLeft = prescriptionResponse.axisLeft
    val axisRight = prescriptionResponse.axisRight
    val additionLeft = prescriptionResponse.additionLeft
    val additionRight = prescriptionResponse.additionRight
    val prismDegreeLeft = prescriptionResponse.prismDegreeLeft
    val prismDegreeRight = prescriptionResponse.prismDegreeRight
    val prismAxisLeft = prescriptionResponse.prismAxisLeft
    val prismAxisRight = prescriptionResponse.prismAxisRight
    val prismPositionLeft = prescriptionResponse.prismPositionLeft
    val prismPositionRight = prescriptionResponse.prismPositionRight

    val isPrismAxisLeftEnabled = hasPrism && prismPositionLeft == PrismPosition.Axis
    val isPrismAxisRightEnabled = hasPrism && prismPositionRight == PrismPosition.Axis

    private val hasPrescriptionLoaded = prescriptionResponseAsync is Success
            && prescriptionResponseAsync.invoke().isRight()
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