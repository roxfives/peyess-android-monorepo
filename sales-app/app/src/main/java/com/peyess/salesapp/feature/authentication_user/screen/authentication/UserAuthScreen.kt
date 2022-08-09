package com.peyess.salesapp.feature.authentication_user.screen.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.feature.authentication_user.screen.authentication.state.UserAuthState
import com.peyess.salesapp.feature.authentication_user.screen.authentication.state.UserAuthViewModel
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.group.CredentialsInput
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun UserAuthScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val viewModel: UserAuthViewModel = mavericksViewModel()

    val collaborator by viewModel.collectAsState(UserAuthState::currentUser)

    val isLoading by viewModel.collectAsState(UserAuthState::isAuthenticating)

    val email by viewModel.collectAsState(UserAuthState::email)
    val hasEmailError by viewModel.collectAsState(UserAuthState::hasUsernameError)
    val emailErrorMessage by viewModel.collectAsState(UserAuthState::emailErrorMessage)

    val password by viewModel.collectAsState(UserAuthState::password)
    val hasPasswordError by viewModel.collectAsState(UserAuthState::hasPasswordError)
    val passwordErrorMessage by viewModel.collectAsState(UserAuthState::passwordErrorMessage)

    val passcode by viewModel.collectAsState(UserAuthState::passcode)
    val hasPasscodeError by viewModel.collectAsState(UserAuthState::hasPasscodeError)
    val passcodeErrorMessage by viewModel.collectAsState(UserAuthState::passcodeErrorMessage)

    val hasError by viewModel.collectAsState(UserAuthState::hasError)
    val errorMessage by viewModel.collectAsState(UserAuthState::authErrorMessage)

    val canSignIn by viewModel.collectAsState(UserAuthState::canSignIn)

    val hasLocalPasscode by viewModel.collectAsState(UserAuthState::hasLocalPassword)
    val isAuthenticated by viewModel.collectAsState(UserAuthState::isAuthenticated)
    val localAuthState by viewModel.collectAsState(UserAuthState::currentUserLocalAuthState)

    val hasNavigatedToPasscode = remember { mutableStateOf(false) }
    if (
        isAuthenticated &&
        (hasLocalPasscode is Success && !hasLocalPasscode.invoke()!!)
    ) {
        LaunchedEffect(Unit) {
            if (!hasNavigatedToPasscode.value) {
                hasNavigatedToPasscode.value = true

                navHostController.navigate(SalesAppScreens.LocalPasscode.name) {
                    popUpTo(SalesAppScreens.UserAuth.name) {
                        inclusive = true
                    }
                }
            }
        }
    }

    val hasNavigatedToHome = remember { mutableStateOf(false) }
    if (
        isAuthenticated &&
        (localAuthState is Success
                && localAuthState.invoke()!! == LocalAuthorizationState.Authorized)
    ) {
        LaunchedEffect(Unit) {
            if (!hasNavigatedToHome.value) {
                hasNavigatedToHome.value = true

                navHostController.navigate(SalesAppScreens.Home.name) {
                    popUpTo(SalesAppScreens.UserListAuthentication.name) {
                        inclusive = false
                    }
                }
            }
        }
    }

    when (collaborator) {
        is Success -> {
            val user = collaborator.invoke()!!

            UserSignIn(
                modifier = modifier,
                isLoading = isLoading,

                currentCollaborator = user,
                isAuthenticated = isAuthenticated,

                username = email,
                hasUsernameError = hasEmailError,
                usernameErrorMessage = emailErrorMessage,
                onUsernameChanged = viewModel::onEmailChanged,

                password = password,
                hasPasswordError = hasPasswordError,
                passwordErrorMessage = passwordErrorMessage,
                onPasswordChanged = viewModel::onPasswordChanged,

                passcode = passcode,
                hasPasscodeError = hasPasscodeError,
                passcodeErrorMessage = passcodeErrorMessage,
                onPasscodeChanged = viewModel::onPasscodeChanged,
                onPasscodeConfirm = viewModel::onPasscodeConfirmed,

                hasError = hasError,
                errorMessage = errorMessage,

                canSignIn = canSignIn,
                onSignIn = viewModel::signIn,
            )
        }

        else ->
            PeyessProgressIndicatorInfinite()
    }


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserSignIn(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,

    currentCollaborator: Collaborator = Collaborator(),
    isAuthenticated: Boolean = false,

    username: String = stringResource(id = R.string.empty_string),
    hasUsernameError: Boolean = false,
    usernameErrorMessage: String = stringResource(id = R.string.empty_string),
    onUsernameChanged: (password: String) -> Unit = {},

    password: String = stringResource(id = R.string.empty_string),
    hasPasswordError: Boolean = false,
    passwordErrorMessage: String = stringResource(id = R.string.empty_string),
    onPasswordChanged: (password: String) -> Unit = {},

    passcode: String = stringResource(id = R.string.empty_string),
    hasPasscodeError: Boolean = false,
    passcodeErrorMessage: String = stringResource(id = R.string.empty_string),
    onPasscodeChanged: (passcode: String) -> Unit = {},
    onPasscodeConfirm: () -> Unit = {},

    hasError: Boolean = false,
    errorMessage: String = "",

    canSignIn: Boolean = true,
    onSignIn: () -> Unit = {},
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            modifier = Modifier
                .size(256.dp)
                .width(256.dp)
                .height(256.dp)
                // Clip image to be shaped as a circle
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentCollaborator.picture)
                .crossfade(true)
                .size(width = 256, height = 256)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_profile_placeholder),
            fallback = painterResource(id = R.drawable.ic_profile_placeholder),
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = !isLoading && !isAuthenticated,
            enter = scaleIn(initialScale = 0f),
            exit = scaleOut(targetScale = 0f),
        ) {
            CredentialsInput(
                usernameLabel = { EmailInputLabel() },
                usernamePlaceHolder = { EmailInputPlaceHolder() },

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
                attemptSignIn = onSignIn
            )
        }

        AnimatedVisibility(
            visible = !isLoading && isAuthenticated,
            enter = scaleIn(initialScale = 0f),
            exit = scaleOut(targetScale = 0f),
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Min),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                PeyessPasswordInput(
                    password = passcode,
                    label = { Text(text = stringResource(id = R.string.label_user_passcode)) },
                    placeholder = { Text(text = stringResource(id = R.string.label_user_passcode)) },
                    onValueChange = onPasscodeChanged,
                    isError = hasPasscodeError,
                    errorMessage = passcodeErrorMessage,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        onPasscodeConfirm()
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SalesAppTheme.dimensions.minimum_touch_target),
                    onClick = onPasscodeConfirm,
                ) {
                    Text(text = stringResource(id = R.string.btn_enter))
                }
            }
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

@Composable
fun EmailInputLabel() {
    Text(text = stringResource(id = R.string.label_user_email))
}

@Composable
fun EmailInputPlaceHolder() {
    Text(text = stringResource(id = R.string.placeholder_user_email))
}