package com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage

import androidx.annotation.StringRes
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state.SecondStepState
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.ui.component.footer.StepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme


@Composable
fun SecondStepScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: SecondStepViewModel = mavericksActivityViewModel()

    val options = viewModel.options
    val workOptions = viewModel.workOptions

    val selected by viewModel.collectAsState(SecondStepState::selected)
    val workSelected by viewModel.collectAsState(SecondStepState::workSelected)

    val showWorkList by viewModel.collectAsState(SecondStepState::showWorkList)
    val showMike by viewModel.collectAsState(SecondStepState::showMike)
    val mikeMessageId by viewModel.collectAsState(SecondStepState::mikeMessageId)

    SecondStepScreenImpl(
        modifier = modifier,

        options = options,
        workOptions = workOptions,

        selected = selected,
        workSelected = workSelected,

        showWorkList = showWorkList,

        showMike = showMike,
        mikeMessageId = mikeMessageId,

        onOptionSelected = viewModel::onOptionSelected,
        onWorkOptionSelected = viewModel::onWorkUsageSelected,

        onNext = onNext,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SecondStepScreenImpl(
    modifier: Modifier = Modifier,

    onOptionSelected: (selected: Int) -> Unit = {},
    onWorkOptionSelected: (selected: Int) -> Unit = {},

    options: Map<Int, String> = emptyMap(),
    workOptions: Map<Int, String> = emptyMap(),

    selected: Int? = null,
    workSelected: Int? = null,

    showWorkList: Boolean = false,

    showMike: Boolean = false,
    @StringRes mikeMessageId: Int = R.string.mike_message_default,

    onNext: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(id = R.string.user_profile_second_step_question),
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
            Spacer(modifier = Modifier.width(4.dp))

            for ((id, option) in options) {
                RadioButton(
                    selected = selected == id,
                    onClick = { onOptionSelected(id) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary,
                    ),
                )
                Text(text = option)

                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showWorkList,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = stringResource(id = R.string.user_profile_choose_work))

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.selectableGroup(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    val density = LocalDensity.current
                    val minimumWidthState = remember {
                        MinimumWidthState()
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    for ((id, option) in workOptions) {
                        Row(
                            modifier = Modifier
                                .minimumWidthModifier(
                                    density = density,
                                    state = minimumWidthState,
                                ),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = workSelected == id,
                                onClick = { onWorkOptionSelected(id) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.primary,
                                ),
                            )

                            Text(text = option)
                        }

                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        AnimatedVisibility(
            visible = showMike,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            MikeBubbleRight(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = stringResource(id = mikeMessageId)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        StepperFooter(
            onNext = onNext
        )
    }
}

@Composable
@Preview
private fun SecondStepScreenImplPreview() {
    SalesAppTheme {
        SecondStepScreenImpl(
            modifier = Modifier.fillMaxWidth(),
            showWorkList = true,
        )
    }
}