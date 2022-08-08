package com.peyess.salesapp.feature.authentication_store

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.authentication_store.state.AuthenticationState
import com.peyess.salesapp.feature.authentication_store.state.AuthenticationViewModel
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val totalLogoWeight = 0.6f
const val totalLogoSpacerWeight = (1f - totalLogoWeight) / 2f

@Composable
fun AuthScreen(modifier: Modifier = Modifier) {
    val viewModel: AuthenticationViewModel = mavericksViewModel()

    val isLoading by viewModel.collectAsState(AuthenticationState::isLoading)

    val username by viewModel.collectAsState(AuthenticationState::username)
    val hasUsernameError by viewModel.collectAsState(AuthenticationState::hasUsernameError)
    val usernameErrorMessage by viewModel.collectAsState(AuthenticationState::usernameErrorMessage)

    val password by viewModel.collectAsState(AuthenticationState::password)
    val hasPasswordError by viewModel.collectAsState(AuthenticationState::hasPasswordError)
    val passwordErrorMessage by viewModel.collectAsState(AuthenticationState::passwordErrorMessage)

    val hasError by viewModel.collectAsState(AuthenticationState::hasError)
    val errorMessage by viewModel.collectAsState(AuthenticationState::errorMessage)

    val canSignIn by viewModel.collectAsState(AuthenticationState::canSignIn)

    val onUsernameChanged = viewModel::updateUsername
    val onPasswordChanged = viewModel::updatePassword

    AuthScreenComposable(
        modifier = modifier,
        isLoading = isLoading,

        username = username,
        hasUsernameError = hasUsernameError,
        usernameErrorMessage = usernameErrorMessage,
        onUsernameChanged = onUsernameChanged,

        password = password,
        hasPasswordError = hasPasswordError,
        passwordErrorMessage = passwordErrorMessage,
        onPasswordChanged = onPasswordChanged,

        hasError = hasError,
        errorMessage = errorMessage,

        canSignIn = canSignIn,
        attemptSignIn = viewModel::signIn,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreenComposable(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    username: String = stringResource(id = R.string.empty_string),
    hasUsernameError: Boolean = false,
    usernameErrorMessage: String = stringResource(id = R.string.empty_string),
    onUsernameChanged: (password: String) -> Unit = {},

    password: String = stringResource(id = R.string.empty_string),
    hasPasswordError: Boolean = false,
    passwordErrorMessage: String = stringResource(id = R.string.empty_string),
    onPasswordChanged: (password: String) -> Unit = {},

    hasError: Boolean = false,
    errorMessage: String = "",

    canSignIn: Boolean = true,
    attemptSignIn: () -> Unit = {},
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SalesAppTheme.dimensions.plane_6),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(totalLogoSpacerWeight))



            CompanyLogo(
                modifier = Modifier
                    .weight(totalLogoWeight)
            )

            Spacer(modifier = Modifier.weight(totalLogoSpacerWeight))
        }

        AnimatedVisibility(
            visible = !isLoading,
            enter = scaleIn(initialScale = 0f),
            exit = scaleOut(targetScale = 0f),
        ) {
            CredentialsInput(
                username = username,
                hasUsernameError = hasUsernameError,
                usernameErrorMessage = usernameErrorMessage,
                onUsernameChanged = onUsernameChanged,

                password = password,
                hasPasswordError = hasPasswordError,
                passwordErrorMessage = passwordErrorMessage,
                onPasswordChanged = onPasswordChanged,

                hasError = hasError,
                errorMessage = errorMessage,

                canSignIn = canSignIn,
                attemptSignIn = attemptSignIn
            )
        }

        AnimatedVisibility(
            modifier = Modifier.padding(SalesAppTheme.dimensions.plane_5),
            visible = isLoading,
            enter = scaleIn(initialScale = 0f),
            exit = scaleOut(targetScale = 0f),
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CredentialsInput(
    modifier: Modifier = Modifier,

    username: String = stringResource(id = R.string.empty_string),
    hasUsernameError: Boolean = false,
    usernameErrorMessage: String = stringResource(id = R.string.empty_string),
    onUsernameChanged: (password: String) -> Unit = {},

    password: String = stringResource(id = R.string.empty_string),
    hasPasswordError: Boolean = false,
    passwordErrorMessage: String = stringResource(id = R.string.empty_string),
    onPasswordChanged: (password: String) -> Unit = {},

    hasError: Boolean = false,
    errorMessage: String = stringResource(id = R.string.empty_string),

    canSignIn: Boolean = true,
    attemptSignIn: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        PeyessOutlinedTextField(
            value = username,
            onValueChange = { onUsernameChanged(it) },
            isError = hasUsernameError,
            errorMessage = stringResource(id = R.string.empty_string),
            placeholder = { EmailInputPlaceHolder() },
            label = { EmailInputLabel() },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(focusDirection = FocusDirection.Down)
            })
        )

        PeyessPasswordInput(
            password = password,
            onValueChange = { onPasswordChanged(it) },
            isError = hasPasswordError,
            errorMessage = stringResource(id = R.string.empty_string),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
                attemptSignIn()
            })
        )

        Spacer(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.plane_5)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(SalesAppTheme.dimensions.minimum_touch_target),
            enabled = canSignIn,
            onClick = attemptSignIn,
        ) {
            Text(
                text = stringResource(id = R.string.btn_sign_in_store)
                    .capitalize(Locale.current)
            )
        }

        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            if (hasError) {
                SignInErrorMessage(errorMessage = errorMessage)
            }

            if (hasUsernameError) {
                SignInErrorMessage(errorMessage = usernameErrorMessage)
            }

            if (hasPasswordError) {
                SignInErrorMessage(errorMessage = passwordErrorMessage)
            }
        }
    }
}

@Composable
fun SignInErrorMessage(
    modifier: Modifier = Modifier,
    errorMessage: String = ""
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            tint = MaterialTheme.colors.error,
            imageVector = Icons.Filled.Error,
            contentDescription = "",
        )

        Spacer(
            modifier = Modifier
                .width(SalesAppTheme.dimensions.grid_1)
        )

        Text(
            modifier = Modifier
                .padding(vertical = SalesAppTheme.dimensions.grid_3),
            text = errorMessage
                .capitalize(Locale.current),
            color = MaterialTheme.colors.error,
        )
    }
}

@Composable
fun EmailInputLabel() {
    Text(text = stringResource(id = R.string.label_username_input))
}

@Composable
fun EmailInputPlaceHolder() {
    Text(text = stringResource(id = R.string.placeholder_username_input))
}

@Composable
fun CompanyLogo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_logo_peyess_light_bg),
        contentDescription = null,
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AuthScreenPreview() {
    SalesAppTheme {
        AuthScreenComposable(isLoading = false)
    }
}
