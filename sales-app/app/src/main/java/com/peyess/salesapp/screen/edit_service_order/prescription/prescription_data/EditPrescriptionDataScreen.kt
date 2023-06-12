package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.prescription.prescription_data.PrescriptionDataUI
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.state.EditPrescriptionDataState
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.state.EditPrescriptionDataViewModel
import com.peyess.salesapp.screen.edit_service_order.prescription.utils.ParseParameters

@Composable
fun EditPrescriptionDataScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onShowSymptoms: (saleId: String, serviceOrderId: String, prescriptionId: String) -> Unit = { _, _, _ -> },
    onNext: () -> Unit = {},
) {

    val viewModel: EditPrescriptionDataViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onSetSaleId,
        onUpdateServiceOrderId = viewModel::onSetServiceOrderId,
        onUpdatePrescriptionId = viewModel::onSetPrescriptionId,
    )

    val saleId by viewModel.collectAsState(EditPrescriptionDataState::saleId)
    val serviceOrderId by viewModel.collectAsState(EditPrescriptionDataState::serviceOrderId)
    val prescriptionId by viewModel.collectAsState(EditPrescriptionDataState::prescriptionId)

    val sphericalLeft by viewModel.collectAsState(EditPrescriptionDataState::sphericalLeft)
    val sphericalRight by viewModel.collectAsState(EditPrescriptionDataState::sphericalRight)
    val cylindricalRight by viewModel.collectAsState(EditPrescriptionDataState::cylindricalRight)
    val axisRight by viewModel.collectAsState(EditPrescriptionDataState::axisRight)
    val additionRight by viewModel.collectAsState(EditPrescriptionDataState::additionRight)
    val prismDegreeRight by viewModel.collectAsState(EditPrescriptionDataState::prismDegreeRight)
    val prismAxisRight by viewModel.collectAsState(EditPrescriptionDataState::prismAxisRight)
    val cylindricalLeft by viewModel.collectAsState(EditPrescriptionDataState::cylindricalLeft)
    val axisLeft by viewModel.collectAsState(EditPrescriptionDataState::axisLeft)
    val additionLeft by viewModel.collectAsState(EditPrescriptionDataState::additionLeft)
    val prismDegreeLeft by viewModel.collectAsState(EditPrescriptionDataState::prismDegreeLeft)
    val prismAxisLeft by viewModel.collectAsState(EditPrescriptionDataState::prismAxisLeft)
    val prismAxisPositionLeft by viewModel.collectAsState(EditPrescriptionDataState::prismPositionLeft)
    val prismAxisPositionRight by viewModel.collectAsState(EditPrescriptionDataState::prismPositionRight)

    val isMessageLoading by viewModel.collectAsState(EditPrescriptionDataState::isMessageLoading)
    val isAnimationLoading by viewModel.collectAsState(EditPrescriptionDataState::isAnimationLoading)
    val hasAxisRight by viewModel.collectAsState(EditPrescriptionDataState::hasAxisRight)
    val hasAxisLeft by viewModel.collectAsState(EditPrescriptionDataState::hasAxisLeft)
    val hasAddition by viewModel.collectAsState(EditPrescriptionDataState::hasAddition)
    val hasPrism by viewModel.collectAsState(EditPrescriptionDataState::hasPrism)
    val isPrismAxisRightEnabled by viewModel.collectAsState(EditPrescriptionDataState::isPrismAxisRightEnabled)
    val isPrismAxisLeftEnabled by viewModel.collectAsState(EditPrescriptionDataState::isPrismAxisLeftEnabled)

    val generalMessage by viewModel.collectAsState(EditPrescriptionDataState::generalMessage)
    val animationId by viewModel.collectAsState(EditPrescriptionDataState::animationId)

    val observation by viewModel.collectAsState(EditPrescriptionDataState::observationInput)

    PrescriptionDataUI(
        modifier = modifier,
        isMessageLoading = isMessageLoading,
        generalMessage = generalMessage,
        isAnimationLoading = isAnimationLoading,
        animationId = animationId,
        hasAxisRight = hasAxisRight,
        hasAxisLeft = hasAxisLeft,
        hasAddition = hasAddition,
        hasPrism = hasPrism,
        toggleHasPrism = viewModel::toggleHasPrism,
        isPrismAxisRightEnabled = isPrismAxisRightEnabled,
        isPrismAxisLeftEnabled = isPrismAxisLeftEnabled,
        sphericalLeft = sphericalLeft,
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
        prismAxisPositionRight = prismAxisPositionRight,
        onPrismAxisLeftPicked = viewModel::setPrismPositionLeft,
        onPrismAxisRightPicked = viewModel::setPrismPositionRight,

        observation = observation,
        onObservationUpdate = viewModel::updateObservation,

        onNext = onNext,
        onShowSymptoms = { onShowSymptoms(saleId, serviceOrderId, prescriptionId) },
    )
}