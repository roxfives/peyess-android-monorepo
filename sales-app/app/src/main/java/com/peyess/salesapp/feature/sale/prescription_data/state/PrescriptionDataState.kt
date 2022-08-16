package com.peyess.salesapp.feature.sale.prescription_data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.model.products.LensTypeCategory

data class PrescriptionDataState(
    val currentPrescriptionData: Async<PrescriptionDataEntity> = Uninitialized,
    val hasAdditionAsync: Async<Boolean> = Uninitialized,
    val mikeMessage: Async<String> = Uninitialized,
): MavericksState {
    internal val _currentPrescriptionData = currentPrescriptionData.invoke()
    val isLoading = currentPrescriptionData is Success && _currentPrescriptionData == null
    val isMikeLoading = mikeMessage is Loading

    val hasAddition = hasAdditionAsync is Success && hasAdditionAsync.invoke()
    val hasPrism = _currentPrescriptionData?.hasPrism ?: false

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
}
