package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeState
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import timber.log.Timber


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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(
                        id = R.string.user_profile_previous_experience_classification,
                    ),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )


//                Slider(
//                    modifier = Modifier.weight(6f),
//                    value =  usageScore,
//                    onValueChange = {
//                        onScoreChanged(it)
//                    },
//                    valueRange = 0f..5f,
//                    steps = 6,
//                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val locale = remember { java.util.Locale.getDefault() }

                    var rating by remember { mutableStateOf(usageScore) }
                    val config = remember {
                        RatingBarConfig()
                            .stepSize(StepSize.HALF)
                            .size(42.dp)
                            .padding(4.dp)
                            .style(RatingBarStyle.HighLighted)
                    }

                    RatingBar(
                        value = rating,
                        config = config,
                        onValueChange = {
                            rating = it
                        },
                        onRatingChanged = {
                            onScoreChanged(it)
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "%,.2f".format(locale, rating),
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    )
                }
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