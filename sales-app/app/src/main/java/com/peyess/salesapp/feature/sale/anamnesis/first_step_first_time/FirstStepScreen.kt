package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeState
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.math.RoundingMode


@Composable
fun FirstTimeScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: FirstTimeViewModel = mavericksActivityViewModel()

    val firstTime by viewModel.collectAsState(FirstTimeState::isFirstTime)

    val score by viewModel.collectAsState(FirstTimeState::usageScore)
    val showUsageScore by viewModel.collectAsState(FirstTimeState::showUsageScore)

    val mikeMessage by viewModel.collectAsState(FirstTimeState::mikeMessage)

    FirstTimeScreenImpl(
        modifier = modifier,

        onSelectFirstTimeUsage = viewModel::onFirstTimeChanged,
        onScoreChanged = viewModel::onUsageScoreChanged,

        firstTime = firstTime,
        usageScore = score,
        showUsageScore = showUsageScore,

        mikeMessage = mikeMessage,

        onNext = onNext,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FirstTimeScreenImpl(
    modifier: Modifier = Modifier,

    onSelectFirstTimeUsage: (firstTime: Boolean) -> Unit = {},
    onScoreChanged: (score: Float) -> Unit = {},

    firstTime: Boolean? = null,
    showUsageScore: Boolean = false,
    usageScore: Float = 0f,

    mikeMessage: String = "",

    onNext: () -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState(true)
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            // TODO: Use string resource
            positiveButton("Vamos lá!")
        }
    ) {
        MikeBubbleRight(
            modifier = Modifier
                .height(360.dp)
                .padding(16.dp),
            text = mikeMessage,
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.user_profile_first_step_question),
            style = MaterialTheme.typography.h6.copy(
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = firstTime ?: false,
                onClick = { onSelectFirstTimeUsage(true) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary,
                ),
            )
            Text(text = stringResource(id = R.string.anamnesis_first_step_yes))

            Spacer(modifier = Modifier.width(4.dp))

            RadioButton(
                selected = !(firstTime ?: true),
                onClick = { onSelectFirstTimeUsage(false) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary,
                ),
            )
            Text(text = stringResource(id = R.string.anamnesis_first_step_no))
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = showUsageScore,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.weight(2f),
                    text = stringResource(
                        id = R.string.user_profile_previous_experience_classification,
                    ),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Slider(
                    modifier = Modifier.weight(6f),
                    value =  usageScore,
                    onValueChange = {
                        onScoreChanged(it)
                    },
                    valueRange = 0f..5f,
                    steps = 6,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = "${usageScore.toInt()}",
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PeyessNextStep(
            onNext = onNext
        )
    }
}

@Composable
@Preview
private fun FirstTimeScreenImplPreview() {
    SalesAppTheme {
        FirstTimeScreenImpl(
            modifier = Modifier.fillMaxWidth(),
            showUsageScore = true,
        )
    }
}