package com.peyess.salesapp.screen.sale.prescription_data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.prescription.prescription_symptoms.PrescriptionSymptomsUI
import com.peyess.salesapp.screen.sale.prescription_data.state.PrescriptionDataState
import com.peyess.salesapp.screen.sale.prescription_data.state.PrescriptionDataViewModel
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite

@Composable
fun PrescriptionSymptomsScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: PrescriptionDataViewModel = mavericksViewModel()

    val isLoading by viewModel.collectAsState(PrescriptionDataState::isLoading)

    val mikeMessageAmetropies by viewModel.collectAsState(PrescriptionDataState::mikeMessageAmetropies)

    val hasHypermetropiaLeft by viewModel.collectAsState(PrescriptionDataState::hasHypermetropiaLeft)
    val hasMyopiaLeft by viewModel.collectAsState(PrescriptionDataState::hasMyopiaLeft)
    val hasAstigmatismLeft by viewModel.collectAsState(PrescriptionDataState::hasAstigmatismLeft)
    val hasPresbyopiaLeft by viewModel.collectAsState(PrescriptionDataState::hasPresbyopiaLeft)
    val hasHypermetropiaRight by viewModel.collectAsState(PrescriptionDataState::hasHypermetropiaRight)
    val hasMyopiaRight by viewModel.collectAsState(PrescriptionDataState::hasMyopiaRight)
    val hasAstigmatismRight by viewModel.collectAsState(PrescriptionDataState::hasAstigmatismRight)
    val hasPresbyopiaRight by viewModel.collectAsState(PrescriptionDataState::hasPresbyopiaRight)

    if (isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        PrescriptionSymptomsUI(
            modifier = modifier,
            onDone = onNext,
            mikeMessageAmetropies = mikeMessageAmetropies,
            hasHypermetropia = hasHypermetropiaLeft || hasHypermetropiaRight,
            hasMyopia = hasMyopiaLeft || hasMyopiaRight,
            hasAstigmatism = hasAstigmatismLeft || hasAstigmatismRight,
            hasPresbyopia = hasPresbyopiaLeft || hasPresbyopiaRight,
        )
    }
}
