package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.prescription.prescription_symptoms.PrescriptionSymptomsUI
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.state.EditPrescriptionSymptomsState
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.state.EditPrescriptionSymptomsViewModel
import com.peyess.salesapp.screen.edit_service_order.prescription.utils.ParseParameters

@Composable
fun EditPrescriptionSymptomsScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onNext: () -> Unit = {},
) {
    val viewModel: EditPrescriptionSymptomsViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onSetSaleId,
        onUpdateServiceOrderId = viewModel::onSetServiceOrderId,
        onUpdatePrescriptionId = viewModel::onSetPrescriptionId,
    )

    val mikeMessageAmetropies by viewModel.collectAsState(EditPrescriptionSymptomsState::mikeMessageAmetropies)

    val hasHypermetropiaLeft by viewModel.collectAsState(EditPrescriptionSymptomsState::hasHypermetropiaLeft)
    val hasMyopiaLeft by viewModel.collectAsState(EditPrescriptionSymptomsState::hasMyopiaLeft)
    val hasAstigmatismLeft by viewModel.collectAsState(EditPrescriptionSymptomsState::hasAstigmatismLeft)
    val hasPresbyopiaLeft by viewModel.collectAsState(EditPrescriptionSymptomsState::hasPresbyopiaLeft)
    val hasHypermetropiaRight by viewModel.collectAsState(EditPrescriptionSymptomsState::hasHypermetropiaRight)
    val hasMyopiaRight by viewModel.collectAsState(EditPrescriptionSymptomsState::hasMyopiaRight)
    val hasAstigmatismRight by viewModel.collectAsState(EditPrescriptionSymptomsState::hasAstigmatismRight)
    val hasPresbyopiaRight by viewModel.collectAsState(EditPrescriptionSymptomsState::hasPresbyopiaRight)

    PrescriptionSymptomsUI(
        modifier = modifier,
        mikeMessageAmetropies = mikeMessageAmetropies,
        hasHypermetropia = hasHypermetropiaLeft || hasHypermetropiaRight,
        hasMyopia = hasMyopiaLeft || hasMyopiaRight,
        hasAstigmatism = hasAstigmatismLeft || hasAstigmatismRight,
        hasPresbyopia = hasPresbyopiaLeft || hasPresbyopiaRight,
        onDone = onNext,
    )
}
