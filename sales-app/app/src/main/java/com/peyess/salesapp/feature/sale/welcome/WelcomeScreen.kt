package com.peyess.salesapp.feature.sale.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.welcome.state.WelcomeState
import com.peyess.salesapp.feature.sale.welcome.state.WelcomeViewModel
import com.peyess.salesapp.ui.component.footer.StepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: WelcomeViewModel = mavericksViewModel()

    val isLoading by viewModel.collectAsState(WelcomeState::isLoading)

    val collaborator by viewModel.collectAsState(WelcomeState::collaboratorDocument)
    val clientName by viewModel.collectAsState(WelcomeState::clientName)

    val canGoNext by viewModel.collectAsState(WelcomeState::canGoNext)
    val hasError by viewModel.collectAsState(WelcomeState::hasError)

    if (!isLoading) {
        val collaboratorName = collaborator.invoke()!!.name

        WelcomeScreenImpl(
            modifier = modifier.fillMaxSize(),
            collaboratorName = collaboratorName,
            clientName = clientName,
            hasError = hasError,
            errorMessage = stringResource(id = R.string.error_client_name_empty),
            onClientNameChanged = viewModel::onClientNameChanged,
            canGoNext = canGoNext,
            onDone = {
                if (canGoNext) {
                    onNext()
                }
            },
        )
    } else {
        PeyessProgressIndicatorInfinite()
    }
}

@Composable
private fun WelcomeScreenImpl(
    modifier: Modifier = Modifier,
    hasError: Boolean = false,
    errorMessage: String = "",
    collaboratorName: String = "",
    clientName: String = "",
    onClientNameChanged: (name: String) -> Unit = {},
    canGoNext: Boolean = false,
    onDone: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        MikeBubbleRight(modifier = Modifier
            .weight(1f)
            .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.mike_welcome).format(collaboratorName))

        Spacer(modifier = Modifier.height(SalesAppTheme.dimensions.plane_7))

        Column(modifier = Modifier
            .width(IntrinsicSize.Min)
            .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {
            PeyessOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = clientName,
                onValueChange = onClientNameChanged,
                isError = hasError,
                errorMessage = errorMessage,
                label = { Text(text = stringResource(id = R.string.label_client_name)) },
                placeholder = { Text(text = stringResource(id = R.string.label_client_name)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                ),
                keyboardActions = KeyboardActions(onDone = { onDone() }),
            )
        }

        StepperFooter(
            canGoNext = canGoNext,
            onNext = onDone,
        )
    }
}

@Preview
@Composable
private fun WelcomePreview() {
    SalesAppTheme {
        WelcomeScreenImpl(
            modifier = Modifier.padding(SalesAppTheme.dimensions.screen_offset),
            clientName = "superName",
            collaboratorName = "superHiperName",
        )
    }
}