package com.peyess.salesapp.feature.sale.prescription_data

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.prescription_data.state.PrescriptionDataState
import com.peyess.salesapp.feature.sale.prescription_data.state.PrescriptionDataViewModel
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val mikeHeight = 300.dp

@Composable
fun PrescriptionDataSymptomsScreen(
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
        PrescriptionDataSymptomsScreenImpl(
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



@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PrescriptionDataSymptomsScreenImpl(
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},

    mikeMessageAmetropies: String = "",

    hasHypermetropia: Boolean = true,
    hasMyopia: Boolean = true,
    hasAstigmatism: Boolean = true,
    hasPresbyopia: Boolean = true,
) {
    val showingCuriosities = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = !showingCuriosities.value,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Spacer(modifier = Modifier.weight(1f))

            MikeBubbleRight(
                modifier = Modifier.height(mikeHeight),
                text = mikeMessageAmetropies,
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        AnimatedVisibility(
            visible = showingCuriosities.value,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            SymptomsAndCuriosities(
                hasHypermetropia = hasHypermetropia,
                hasMyopia = hasMyopia,
                hasAstigmatism = hasAstigmatism,
                hasPresbyopia = hasPresbyopia,
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            if (showingCuriosities.value) {
                Box(modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target)) {
                    Button(
                        modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                        onClick = {
                            showingCuriosities.value = false
                            onDone()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.dialog_curiosities_return_button))
                    }
                }
            } else {
                Box(modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target)) {
                    Button(
                        modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                        onClick = { showingCuriosities.value = true },
                    ) {
                        Text(text = stringResource(id = R.string.dialog_curiosities_show_me_button))
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomsAndCuriosities(
    modifier: Modifier = Modifier,

    hasHypermetropia: Boolean = true,
    hasMyopia: Boolean = true,
    hasAstigmatism: Boolean = true,
    hasPresbyopia: Boolean = true,
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        if (hasMyopia) {
            SymptomsAndCuriositiesContent(
                title = stringResource(id = R.string.myopia),
                symptoms = stringResource(id = R.string.dialog_symptoms_myopia),
                curiosities = stringResource(id = R.string.dialog_curiosities_myopia),
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }

        if (hasAstigmatism) {
            SymptomsAndCuriositiesContent(
                title = stringResource(id = R.string.astigmatism),
                symptoms = stringResource(id = R.string.dialog_symptoms_astigmatism),
                curiosities = stringResource(id = R.string.dialog_curiosities_astigmatism),
            )
            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }

        if (hasPresbyopia) {
            SymptomsAndCuriositiesContent(
                title = stringResource(id = R.string.presbyopia),
                symptoms = stringResource(id = R.string.dialog_symptoms_presbyopia),
                curiosities = stringResource(id = R.string.dialog_curiosities_presbyopia),
            )

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }

        if (hasHypermetropia) {
            SymptomsAndCuriositiesContent(
                title = stringResource(id = R.string.hypermetropia),
                symptoms = stringResource(id = R.string.dialog_symptoms_hypermetropia),
                curiosities = stringResource(id = R.string.dialog_curiosities_hypermetropia),
            )

            Divider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
            )
        }
    }
}

@Composable
private fun SymptomsAndCuriositiesContent(
    modifier: Modifier = Modifier,

    title: String = "",
    symptoms: String = "",
    curiosities: String = "",
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = stringResource(id = R.string.dialog_symptoms),
            style = MaterialTheme.typography.h6.copy(textIndent = TextIndent(8.sp, 8.sp)),
        )
        Text(
            text = symptoms,
            style = MaterialTheme.typography.body1.copy(textIndent = TextIndent(18.sp, 18.sp)),
        )

        Text(
            text = stringResource(id = R.string.curiosities),
            style = MaterialTheme.typography.h6.copy(textIndent = TextIndent(8.sp, 8.sp)),
        )
        Text(
            text = curiosities,
            style = MaterialTheme.typography.body1.copy(textIndent = TextIndent(18.sp, 18.sp)),
        )
    }
}

@Preview
@Composable
private fun SymptomsAndCuriositiesPreview() {
    SalesAppTheme {
        SymptomsAndCuriosities(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun SymptomsAndCuriositiesContentPreview() {
    SalesAppTheme {
        SymptomsAndCuriositiesContent(
            modifier = Modifier.fillMaxWidth(),

            title = stringResource(id = R.string.myopia),
            symptoms = stringResource(id = R.string.dialog_symptoms_myopia),
            curiosities = stringResource(id = R.string.dialog_curiosities_myopia),
        )
    }
}




