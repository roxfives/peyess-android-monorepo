package com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state.FifthStepState
import com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.theme.SalesAppTheme


@Composable
fun FifthStepScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: FifthStepViewModel = mavericksActivityViewModel()

    val options = viewModel.options

    val selected by viewModel.collectAsState(FifthStepState::selected)

    val showMike by viewModel.collectAsState(FifthStepState::showMike)
    val mikeMessageId by viewModel.collectAsState(FifthStepState::mikeMessageId)

    FifthStepScreenImpl(
        modifier = modifier,

        options = options,
        selected = selected,

        showMike = showMike,
        mikeMessageId = mikeMessageId,

        onOptionSelected = viewModel::onOptionSelected,

        onNext = onNext,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FifthStepScreenImpl(
    modifier: Modifier = Modifier,

    onOptionSelected: (selected: Int) -> Unit = {},

    options: Map<Int, String> = emptyMap(),

    selected: Int? = null,

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
            text = stringResource(id = R.string.user_profile_fifth_step_question),
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

        PeyessStepperFooter(
            onNext = onNext
        )
    }
}

@Composable
@Preview
private fun FifthStepScreenImplPreview() {
    SalesAppTheme {
        FifthStepScreenImpl(
            modifier = Modifier.fillMaxWidth(),
        )
    }
}