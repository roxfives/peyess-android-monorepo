package com.peyess.salesapp.feature.authentication_user.authentication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.authentication_store.CompanyLogo
import com.peyess.salesapp.feature.authentication_store.CredentialsInput
import com.peyess.salesapp.feature.authentication_user.authentication.state.UserAuthState
import com.peyess.salesapp.feature.authentication_user.authentication.state.UserAuthViewModel
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val totalLogoWeight = 0.6f
const val totalLogoSpacerWeight = (1f - totalLogoWeight) / 2f

@Composable
fun UserAuthScreen(modifier: Modifier = Modifier) {
    val viewModel: UserAuthViewModel = mavericksViewModel()

    val collaborator by viewModel.collectAsState(UserAuthState::currentUser)

    val isLoading by viewModel.collectAsState(UserAuthState::isAuthenticating)

    val email by viewModel.collectAsState(UserAuthState::email)
//    val hasUsernameError by viewModel.collectAsState(UserAuthState::hasUsernameError)
//    val usernameErrorMessage by viewModel.collectAsState(UserAuthState::usernameErrorMessage)

    val password by viewModel.collectAsState(UserAuthState::password)
//    val hasPasswordError by viewModel.collectAsState(UserAuthState::hasPasswordError)
//    val passwordErrorMessage by viewModel.collectAsState(UserAuthState::passwordErrorMessage)

    val hasError by viewModel.collectAsState(UserAuthState::hasError)
    val errorMessage by viewModel.collectAsState(UserAuthState::authErrorMessage)

    val canSignIn by viewModel.collectAsState(UserAuthState::canSignIn)

    when (collaborator) {
        is Success -> {
            val user = collaborator.invoke()!!

            UserSignIn(
                modifier = modifier,
                isLoading = isLoading,

                currentCollaborator = user,

                username = email,
                hasUsernameError = false, // hasUsernameError,
                usernameErrorMessage = "", // usernameErrorMessage,
                onUsernameChanged = viewModel::onEmailChanged,

                password = password,
                hasPasswordError = false, // hasPasswordError,
                passwordErrorMessage = "", // passwordErrorMessage,
                onPasswordChanged = viewModel::onPasswordChanged,

                hasError = hasError,
                errorMessage = errorMessage,

                canSignIn = canSignIn,
                onSignIn = viewModel::regularSignIn,
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
            error = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            fallback = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            placeholder = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                attemptSignIn = onSignIn
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


//@OptIn(ExperimentalAnimationApi::class)
//@Composable
//fun UserSignIn(
//    modifier: Modifier = Modifier,
//    user: Collaborator = Collaborator(),
//
//    isAuthenticating: Boolean = false,
//
//    hasLocalSignIn: Boolean = false,
//    onSignIn: () -> Unit,
//    onLocalSignIn: () -> Unit,
//
//    password: String = "",
//    onPasswordChanged: (value: String) -> Unit = {},
//) {
//    Column(
//        modifier = modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//    ) {
//        AsyncImage(
//            modifier = Modifier
//                .size(256.dp)
//                .width(256.dp)
//                .height(256.dp)
//                // Clip image to be shaped as a circle
//                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
//                .clip(CircleShape),
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(user.picture)
//                .crossfade(true)
//                .size(width = 256, height = 256)
//                .build(),
//            contentScale = ContentScale.FillBounds,
//            contentDescription = "",
//            error = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
//            fallback = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
//            placeholder = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        AnimatedVisibility(
//            visible = !isAuthenticating,
//            enter = scaleIn(),
//            exit = scaleOut(),
//        ) {
//            if (hasLocalSignIn) {
//                SignIn(
//                    password = password,
//                    onPasswordChanged = onPasswordChanged,
//                    message = stringResource(id = R.string.message_local_sign_in),
//                    onSignIn = onLocalSignIn,
//                )
//            } else {
//                SignIn(
//                    password = password,
//                    onPasswordChanged = onPasswordChanged,
//                    message = stringResource(id = R.string.message_regular_sign_in),
//                    onSignIn = onSignIn,
//                )
//            }
//        }
//
//        AnimatedVisibility(
//            visible = isAuthenticating,
//            enter = scaleIn(),
//            exit = scaleOut(),
//        ) {
//            CircularProgressIndicator()
//        }
//    }
//}

//@OptIn(ExperimentalComposeUiApi::class)
//@Composable
//fun SignIn(
//    modifier: Modifier = Modifier,
//    message: String = "",
//    password: String = "",
//    hasError: Boolean = false,
//    errorMessage: String = "",
//    onPasswordChanged: (value: String) -> Unit = {},
//    onSignIn: () -> Unit = {},
//) {
//    Column(
//        modifier = Modifier.width(IntrinsicSize.Min),
//        horizontalAlignment = Alignment.CenterHorizontally,
//    ) {
//        val keyboardController = LocalSoftwareKeyboardController.current
//
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Icon(
//                modifier = Modifier.padding(horizontal = 6.dp),
//                imageVector = Icons.Filled.Info,
//                tint = MaterialTheme.colors.primary,
//                contentDescription = ""
//            )
//
//            Text(text = message, style = MaterialTheme.typography.body1)
//        }
//
//        PeyessPasswordInput(password = password,
//            onValueChange = onPasswordChanged,
//            isError = hasError,
//            errorMessage = errorMessage,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
//            keyboardActions = KeyboardActions(onGo = {
//                keyboardController?.hide()
//                onSignIn()
//            }))
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(SalesAppTheme.dimensions.minimum_touch_target),
//            onClick = onSignIn
//        ) {
//            Text(text = stringResource(id = R.string.btn_enter))
//        }
//    }
//}