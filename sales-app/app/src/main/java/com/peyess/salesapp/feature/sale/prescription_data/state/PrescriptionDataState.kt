package com.peyess.salesapp.feature.sale.prescription_data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.prescription.PrismPosition

data class PrescriptionDataState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val activeServiceOrderResponse: ActiveSOEntity = ActiveSOEntity(),

    val clientName: Async<String> = Uninitialized,
    val currentPrescriptionDataAsync: Async<PrescriptionDataEntity> = Uninitialized,
    val lensTypeCategoryName: Async<LensTypeCategoryName> = Uninitialized,
    val hasAdditionAsync: Async<Boolean> = Uninitialized,
    val mikeMessageAmetropies: String = "",
    val animationId: Async<Int> = Uninitialized,
    val generalMessage: Async<String> = Uninitialized,
): MavericksState {
    val saleId: String = activeServiceOrderResponse.saleId
    val serviceOrderId: String = activeServiceOrderResponse.id

    internal val currentPrescriptionData = currentPrescriptionDataAsync.invoke()
    val isLoading = currentPrescriptionDataAsync is Success && currentPrescriptionData == null
    val isMessageLoading = generalMessage is Loading

    val isAnimationLoading = animationId is Loading

    val hasAddition = hasAdditionAsync is Success && hasAdditionAsync.invoke()
    val hasPrism = currentPrescriptionData?.hasPrism ?: false

    val hasAxisLeft = (currentPrescriptionData?.cylindricalLeft ?: 0.0) < 0.0
    val hasAxisRight = (currentPrescriptionData?.cylindricalRight ?: 0.0) < 0.0

    val sphericalLeft = currentPrescriptionData?.sphericalLeft ?: 0.0
    val sphericalRight = currentPrescriptionData?.sphericalRight ?: 0.0
    val cylindricalLeft = currentPrescriptionData?.cylindricalLeft ?: 0.0
    val cylindricalRight = currentPrescriptionData?.cylindricalRight ?: 0.0
    val axisLeft = currentPrescriptionData?.axisLeft ?: 0.0
    val axisRight = currentPrescriptionData?.axisRight ?: 0.0
    val additionLeft = currentPrescriptionData?.additionLeft ?: 0.75
    val additionRight = currentPrescriptionData?.additionRight ?: 0.75
    val prismDegreeLeft = currentPrescriptionData?.prismDegreeLeft ?: 0.0
    val prismDegreeRight = currentPrescriptionData?.prismDegreeRight ?: 0.0
    val prismAxisLeft = currentPrescriptionData?.prismAxisLeft ?: 0.0
    val prismAxisRight = currentPrescriptionData?.prismAxisRight ?: 0.0
    val prismPositionLeft = currentPrescriptionData?.prismPositionLeft ?: PrismPosition.None
    val prismPositionRight = currentPrescriptionData?.prismPositionRight ?: PrismPosition.None

    val isPrismAxisLeftEnabled = hasPrism && prismPositionLeft == PrismPosition.Axis
    val isPrismAxisRightEnabled = hasPrism && prismPositionRight == PrismPosition.Axis

    val hasHypermetropiaLeft =  sphericalLeft > 0
            && lensTypeCategoryName is Success
            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaLeft = sphericalLeft < 0;
    val hasAstigmatismLeft = cylindricalLeft < 0;
    val hasPresbyopiaLeft = lensTypeCategoryName is Success
            && (
                lensTypeCategoryName.invoke() is LensTypeCategoryName.Near
                || lensTypeCategoryName.invoke() is LensTypeCategoryName.Multi
            )

    val hasHypermetropiaRight =  sphericalRight > 0
            && lensTypeCategoryName is Success
            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaRight = sphericalRight < 0;
    val hasAstigmatismRight = cylindricalRight < 0;
    val hasPresbyopiaRight = lensTypeCategoryName is Success
            && (
            lensTypeCategoryName.invoke() is LensTypeCategoryName.Near
                    || lensTypeCategoryName.invoke() is LensTypeCategoryName.Multi
            )
}
