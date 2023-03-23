package com.peyess.salesapp.screen.sale.prescription_data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionInsertResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.feature.prescription.model.PrescriptionData
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.prescription.PrismPosition

data class PrescriptionDataState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val activeServiceOrderResponse: ActiveSOEntity = ActiveSOEntity(),

    val createPrescriptionResponseAsync: Async<LocalPrescriptionInsertResponse> = Uninitialized,
    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: PrescriptionData = PrescriptionData(),

//    val lensTypeCategoryName: Async<LensTypeCategoryName> = Uninitialized,
//    val hasAdditionAsync: Async<Boolean> = Uninitialized,
    val mikeMessageAmetropies: String = "",
    val animationId: Int = 0,
    val generalMessage: String = "",

    val isLoading: Boolean = false,
): MavericksState {
    val saleId = activeServiceOrderResponse.saleId
    val serviceOrderId = activeServiceOrderResponse.id

    val clientName = activeServiceOrderResponse.clientName

    val isMessageLoading = prescriptionResponseAsync is Loading
    val isAnimationLoading = prescriptionResponseAsync is Loading

//    val hasAddition = hasAdditionAsync is Success && hasAdditionAsync.invoke()
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

    val lensTypeCategoryName = prescriptionResponse.lensTypeCategory
    val hasAddition = lensTypeCategoryName is LensTypeCategoryName.Multi

    val hasHypermetropiaLeft =  sphericalLeft > 0
            && lensTypeCategoryName !is LensTypeCategoryName.Near
//            && lensTypeCategoryName is Success
//            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaLeft = sphericalLeft < 0;
    val hasAstigmatismLeft = cylindricalLeft < 0;
    val hasPresbyopiaLeft = lensTypeCategoryName is LensTypeCategoryName.Near
            || lensTypeCategoryName is LensTypeCategoryName.Multi

//        lensTypeCategoryName is Success
//            && (
//                lensTypeCategoryName.invoke() is LensTypeCategoryName.Near
//                || lensTypeCategoryName.invoke() is LensTypeCategoryName.Multi
//            )

    val hasHypermetropiaRight =  sphericalRight > 0
            && lensTypeCategoryName !is LensTypeCategoryName.Near
//            && lensTypeCategoryName is Success
//            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaRight = sphericalRight < 0;
    val hasAstigmatismRight = cylindricalRight < 0;
    val hasPresbyopiaRight = lensTypeCategoryName is LensTypeCategoryName.Near
            || lensTypeCategoryName is LensTypeCategoryName.Multi

//        lensTypeCategoryName is Success
//            && (
//            lensTypeCategoryName.invoke() is LensTypeCategoryName.Near
//                    || lensTypeCategoryName.invoke() is LensTypeCategoryName.Multi
//            )
}
