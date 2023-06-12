package com.peyess.salesapp.screen.sale.prescription_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.prescription.prescription_data.PrescriptionDataUI
import com.peyess.salesapp.screen.sale.prescription_data.state.PrescriptionDataState
import com.peyess.salesapp.screen.sale.prescription_data.state.PrescriptionDataViewModel
import com.peyess.salesapp.navigation.sale.prescription.isUpdatingParam
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite

@Composable
fun PrescriptionDataScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onShowSymptoms: () -> Unit = {},
    onNext: (isUpdating: Boolean, saleId: String, serviceOrderId: String) -> Unit = { _, _, _ -> },
) {
    val isUpdatingParam = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getBoolean(isUpdatingParam)
        ?: false

    val viewModel: PrescriptionDataViewModel = mavericksViewModel()

    val saleId by viewModel.collectAsState(PrescriptionDataState::saleId)
    val serviceOrderId by viewModel.collectAsState(PrescriptionDataState::serviceOrderId)

    val isLoading by viewModel.collectAsState(PrescriptionDataState::isLoading)
    val isMessageLoading by viewModel.collectAsState(PrescriptionDataState::isMessageLoading)
    val generalMessage by viewModel.collectAsState(PrescriptionDataState::generalMessage)
    val isAnimationLoading by viewModel.collectAsState(PrescriptionDataState::isAnimationLoading)
    val animationId by viewModel.collectAsState(PrescriptionDataState::animationId)

    val sphericalLeft by viewModel.collectAsState(PrescriptionDataState::sphericalLeft)
    val sphericalRight by viewModel.collectAsState(PrescriptionDataState::sphericalRight)
    val cylindricalLeft by viewModel.collectAsState(PrescriptionDataState::cylindricalLeft)
    val cylindricalRight by viewModel.collectAsState(PrescriptionDataState::cylindricalRight)
    val axisLeft by viewModel.collectAsState(PrescriptionDataState::axisLeft)
    val axisRight by viewModel.collectAsState(PrescriptionDataState::axisRight)
    val additionLeft by viewModel.collectAsState(PrescriptionDataState::additionLeft)
    val additionRight by viewModel.collectAsState(PrescriptionDataState::additionRight)
    val prismDegreeLeft by viewModel.collectAsState(PrescriptionDataState::prismDegreeLeft)
    val prismDegreeRight by viewModel.collectAsState(PrescriptionDataState::prismDegreeRight)
    val prismAxisLeft by viewModel.collectAsState(PrescriptionDataState::prismAxisLeft)
    val prismAxisRight by viewModel.collectAsState(PrescriptionDataState::prismAxisRight)

    val hasAxisLeft by viewModel.collectAsState(PrescriptionDataState::hasAxisLeft)
    val hasAxisRight by viewModel.collectAsState(PrescriptionDataState::hasAxisRight)

    val prismAxisPositionLeft by viewModel.collectAsState(PrescriptionDataState::prismPositionLeft)
    val prismAxisPositionRight by viewModel.collectAsState(PrescriptionDataState::prismPositionRight)

    val hasAddition by viewModel.collectAsState(PrescriptionDataState::hasAddition)
    val hasPrism by viewModel.collectAsState(PrescriptionDataState::hasPrism)

    val isPrismAxisRightEnabled by viewModel.collectAsState(PrescriptionDataState::isPrismAxisRightEnabled)
    val isPrismAxisLeftEnabled by viewModel.collectAsState(PrescriptionDataState::isPrismAxisLeftEnabled)

    val observation by viewModel.collectAsState(PrescriptionDataState::prescriptionObservation)

    if (isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        PrescriptionDataUI(
            modifier = modifier,
            onNext = { onNext(isUpdatingParam, saleId, serviceOrderId) },
            onShowSymptoms = onShowSymptoms,

            isMessageLoading = isMessageLoading,
            generalMessage = generalMessage,

            isAnimationLoading = isAnimationLoading,
            animationId = animationId,

            hasAddition = hasAddition,
            hasPrism = hasPrism,
            toggleHasPrism = viewModel::toggleHasPrism,

            hasAxisLeft = hasAxisLeft,
            hasAxisRight = hasAxisRight,

            sphericalRight = sphericalRight,
            onSphericalRightIncrease = viewModel::increaseSphericalRight,
            onSphericalRightDecrease = viewModel::decreaseSphericalRight,

            cylindricalRight = cylindricalRight,
            onCylindricalRightIncrease = viewModel::increaseCylindricalRight,
            onCylindricalRightDecrease = viewModel::decreaseCylindricalRight,

            axisRight = axisRight,
            onAxisRightIncrease = viewModel::increaseAxisRight,
            onAxisRightDecrease = viewModel::decreaseAxisRight,

            additionRight = additionRight,
            onAdditionRightIncrease = viewModel::increaseAdditionRight,
            onAdditionRightDecrease = viewModel::decreaseAdditionRight,

            prismDegreeRight = prismDegreeRight,
            onPrismDegreeRightIncrease = viewModel::increasePrismDegreeRight,
            onPrismDegreeRightDecrease = viewModel::decreasePrismDegreeRight,

            prismAxisRight = prismAxisRight,
            onPrismAxisRightIncrease = viewModel::increasePrismAxisRight,
            onPrismAxisRightDecrease = viewModel::decreasePrismAxisRight,

            sphericalLeft = sphericalLeft,
            onSphericalLeftIncrease = viewModel::increaseSphericalLeft,
            onSphericalLeftDecrease = viewModel::decreaseSphericalLeft,

            cylindricalLeft = cylindricalLeft,
            onCylindricalLeftIncrease = viewModel::increaseCylindricalLeft,
            onCylindricalLeftDecrease = viewModel::decreaseCylindricalLeft,

            axisLeft = axisLeft,
            onAxisLeftIncrease = viewModel::increaseAxisLeft,
            onAxisLeftDecrease = viewModel::decreaseAxisLeft,

            additionLeft = additionLeft,
            onAdditionLeftIncrease = viewModel::increaseAdditionLeft,
            onAdditionLeftDecrease = viewModel::decreaseAdditionLeft,

            prismDegreeLeft = prismDegreeLeft,
            onPrismDegreeLeftIncrease = viewModel::increasePrismDegreeLeft,
            onPrismDegreeLeftDecrease = viewModel::decreasePrismDegreeLeft,

            prismAxisLeft = prismAxisLeft,
            onPrismAxisLeftIncrease = viewModel::increasePrismAxisLeft,
            onPrismAxisLeftDecrease = viewModel::decreasePrismAxisLeft,

            prismAxisPositionLeft = prismAxisPositionLeft,
            onPrismAxisLeftPicked = viewModel::setPrismPositionLeft,

            prismAxisPositionRight = prismAxisPositionRight,
            onPrismAxisRightPicked = viewModel::setPrismPositionRight,

            isPrismAxisRightEnabled = isPrismAxisRightEnabled,
            isPrismAxisLeftEnabled = isPrismAxisLeftEnabled,

            observation = observation,
            onObservationUpdate = viewModel::updateObservation,
        )
    }
}
