package com.peyess.salesapp.screen.authentication_user.screen.local_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.authentication_user.screen.local_password.state.LocalPasswordState
import com.peyess.salesapp.screen.authentication_user.screen.local_password.state.LocalPasswordViewModel
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun LocalPasswordScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val viewModel: LocalPasswordViewModel = mavericksViewModel()

    val passcode by viewModel.collectAsState(LocalPasswordState::password)
    val passcodeConfirmation by viewModel.collectAsState(LocalPasswordState::passwordConfirmation)

    val setPasscodeProcess by viewModel.collectAsState(LocalPasswordState::setPasscodeProcess)
    val hasFailed by viewModel.collectAsState(LocalPasswordState::hasFailed)
    val errorMessage by viewModel.collectAsState(LocalPasswordState::passwordErrorMessage)

    val hasNavigated = remember { mutableStateOf(false) }
    if (setPasscodeProcess is Success) {
        LaunchedEffect(setPasscodeProcess) {
            if (!hasNavigated.value) {
                hasNavigated.value = true

                navHostController.navigate(SalesAppScreens.Home.name) {
                    popUpTo(SalesAppScreens.LocalPasscode.name) {
                        inclusive = true
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LocalPasscodeComposable(
            onPasscodeChanged = viewModel::onPasscodeChanged,
            passcode = passcode,

            passcodeConfirmation = passcodeConfirmation,
            onPasscodeConfirmationChanged = viewModel::onPasscodeConfirmationChanged,

            hasError = hasFailed,
            errorMessage = errorMessage,
            onConfirm = viewModel::onConfirmPasscode,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LocalPasscodeComposable(
    modifier: Modifier = Modifier,

    passcode: String = "",
    onPasscodeChanged: (value: String) -> Unit = {},

    passcodeConfirmation: String = "",
    onPasscodeConfirmationChanged: (value: String) -> Unit = {},

    onConfirm: () -> Unit,
    hasError: Boolean = false,
    errorMessage: String = "",
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_password_screen)
        )

        LottieAnimation(
            modifier = modifier
                .height(256.dp)
                .width(256.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.body1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            text = stringResource(id = R.string.create_passcode),
        )

        Spacer(modifier = Modifier.height(16.dp))

        PeyessPasswordInput(
            password = passcode,
            label = { Text(text = stringResource(id = R.string.input_enter_passcode)) },
            placeholder = { Text(text = stringResource(id = R.string.input_enter_passcode)) },
            onValueChange = onPasscodeChanged,
            isError = hasError,
            errorMessage = "",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.NumberPassword,
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(focusDirection = FocusDirection.Down)
            })
        )

        PeyessPasswordInput(
            password = passcodeConfirmation,
            label = { Text(text = stringResource(id = R.string.input_confirm_passcode)) },
            placeholder = { Text(text = stringResource(id = R.string.input_confirm_passcode)) },
            onValueChange = onPasscodeConfirmationChanged,
            isError = hasError,
            errorMessage = "",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword,
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                onConfirm()
            })
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .fillMaxWidth(),
            onClick = onConfirm,
        ) {
            Text(text = stringResource(id = R.string.btn_create_passcode))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (hasError) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "",
                    tint = MaterialTheme.colors.error,
                )

                Spacer(modifier = Modifier.width(SalesAppTheme.dimensions.grid_3))

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.error,
                )
            }
        }
    }
}

@Preview
@Composable
fun LocalPasscodePreview() {
    LocalPasscodeComposable(onConfirm = {})
}