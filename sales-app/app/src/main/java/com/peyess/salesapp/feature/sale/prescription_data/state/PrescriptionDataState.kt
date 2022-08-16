package com.peyess.salesapp.feature.sale.prescription_data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition

data class PrescriptionDataState(
    val clientName: Async<String> = Uninitialized,
    val currentPrescriptionData: Async<PrescriptionDataEntity> = Uninitialized,
    val lensTypeCategoryName: Async<LensTypeCategoryName> = Uninitialized,
    val hasAdditionAsync: Async<Boolean> = Uninitialized,
    val mikeMessageAmetropies: String = "",
    val mikeMessageTop: Async<String> = Uninitialized,
): MavericksState {
    internal val _currentPrescriptionData = currentPrescriptionData.invoke()
    val isLoading = currentPrescriptionData is Success && _currentPrescriptionData == null
    val isMikeLoading = mikeMessageTop is Loading

    val hasAddition = hasAdditionAsync is Success && hasAdditionAsync.invoke()
    val hasPrism = _currentPrescriptionData?.hasPrism ?: false

    val hasAxisLeft = (_currentPrescriptionData?.cylindricalLeft ?: 0.0) < 0.0
    val hasAxisRight = (_currentPrescriptionData?.cylindricalRight ?: 0.0) < 0.0

    val sphericalLeft = _currentPrescriptionData?.sphericalLeft ?: 0.0
    val sphericalRight = _currentPrescriptionData?.sphericalRight ?: 0.0
    val cylindricalLeft = _currentPrescriptionData?.cylindricalLeft ?: 0.0
    val cylindricalRight = _currentPrescriptionData?.cylindricalRight ?: 0.0
    val axisLeft = _currentPrescriptionData?.axisLeft ?: 0.0
    val axisRight = _currentPrescriptionData?.axisRight ?: 0.0
    val additionLeft = _currentPrescriptionData?.additionLeft ?: 0.75
    val additionRight = _currentPrescriptionData?.additionRight ?: 0.75
    val prismDegreeLeft = _currentPrescriptionData?.prismDegreeLeft ?: 0.0
    val prismDegreeRight = _currentPrescriptionData?.prismDegreeRight ?: 0.0
    val prismAxisLeft = _currentPrescriptionData?.prismAxisLeft ?: 0.0
    val prismAxisRight = _currentPrescriptionData?.prismAxisRight ?: 0.0
    val prismPositionLeft = _currentPrescriptionData?.prismPositionLeft ?: PrismPosition.None
    val prismPositionRight = _currentPrescriptionData?.prismPositionRight ?: PrismPosition.None

    val isPrismAxisLeftEnabled = hasPrism && prismPositionLeft == PrismPosition.Axis
    val isPrismAxisRightEnabled = hasPrism && prismPositionRight == PrismPosition.Axis

    val hasHypermetropiaLeft =  sphericalLeft > 0
            && lensTypeCategoryName is Success
            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaLeft = sphericalLeft < 0;
    val hasAstigmatismLeft = cylindricalLeft < 0;
    val hasPresbyopiaLeft = lensTypeCategoryName is Success
            && (
                lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
                || lensTypeCategoryName.invoke() !is LensTypeCategoryName.Multi
            )

    val hasHypermetropiaRight =  sphericalRight > 0
            && lensTypeCategoryName is Success
            && lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
    val hasMyopiaRight = sphericalRight < 0;
    val hasAstigmatismRight = cylindricalRight < 0;
    val hasPresbyopiaRight = lensTypeCategoryName is Success
            && (
            lensTypeCategoryName.invoke() !is LensTypeCategoryName.Near
                    || lensTypeCategoryName.invoke() !is LensTypeCategoryName.Multi
            )
}
