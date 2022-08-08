package com.peyess.salesapp.screen.authentication_user

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.screen.authentication_user.state.UserAuthenticationState
import com.peyess.salesapp.screen.authentication_user.state.UserAuthenticationViewModel
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessPasswordInput
import com.peyess.salesapp.ui.theme.SalesAppTheme



@Composable
fun UserAuthScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: UserAuthenticationViewModel = mavericksViewModel()

    val users by viewModel.collectAsState(UserAuthenticationState::users)
    val currentUser by viewModel.collectAsState(UserAuthenticationState::currentUser)
    val store by viewModel.collectAsState(UserAuthenticationState::currentStore)

    val screenState by viewModel.collectAsState(UserAuthenticationState::screenState)
    val password by viewModel.collectAsState(UserAuthenticationState::password)

    val isAuthenticating by viewModel.collectAsState(UserAuthenticationState::isAuthenticating)

    UserAuthScreenComposable(
        modifier = modifier,
        screenState = screenState,
        store = store,

        isAuthenticating = isAuthenticating,

        currentUser = currentUser,
        users = users,

        onEnter = viewModel::enterStore,

        onSignIn = viewModel::regularSignIn,
        onLocalSignIn = viewModel::localSignIn,

        password = password,
        onPasswordChanged = viewModel::onPasswordChanged,
    )
}

@Composable
fun UserAuthScreenComposable(
    modifier: Modifier = Modifier,
    screenState: UserAuthenticationState.ScreenState,
    store: Async<OpticalStore> = Uninitialized,

    isAuthenticating: Boolean = false,

    currentUser: Collaborator = Collaborator(),
    users: List<Collaborator> = listOf(),

    onEnter: (id: String) -> Unit = {},

    onSignIn: () -> Unit = {},
    onLocalSignIn: () -> Unit = {},

    password: String = "",
    onPasswordChanged: (value: String) -> Unit = {},
) {
    when(screenState) {
        UserAuthenticationState.ScreenState.ChooseUser ->
            UserGrid(
                modifier = modifier,
                store = store,
                users = users,

                onEnter = onEnter,
            )

        UserAuthenticationState.ScreenState.SignIn ->
            UserSignIn(
                isAuthenticating = isAuthenticating,
                user = currentUser,
                onSignIn = onSignIn,
                onLocalSignIn = onLocalSignIn,
                password = password,
                onPasswordChanged = onPasswordChanged
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserGrid(
    modifier: Modifier = Modifier,
    store: Async<OpticalStore> = Uninitialized,
    users: List<Collaborator> = listOf(),
    onEnter: (id: String) -> Unit = {},
) {
    if(store is Success) {
        Column(modifier = modifier.fillMaxSize()) {
            Header(store = store.invoke())

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SalesAppTheme.dimensions.plane_4),
            )

            LazyVerticalGrid(
                cells = GridCells.Adaptive(minSize = 256.dp),
            ) {
                items(users.size) {
                    UserBox(
                        modifier = Modifier
                            .padding(SalesAppTheme.dimensions.grid_1),
                        user = users[it],

                        onEnter = onEnter
                    )
                }
            }
        }
    } else {
      PeyessProgressIndicatorInfinite()
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    store: OpticalStore = OpticalStore(),
) {
    Row(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(160.dp)
                .width(160.dp)
                .height(160.dp)
                .border(width = 2.dp, color = MaterialTheme.colors.onPrimary, shape = CircleShape)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(store.picture)
                .crossfade(true)
                .size(width = 256, height = 256)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            fallback = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            placeholder = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column {
            Spacer(modifier = Modifier.size(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    imageVector = Icons.Filled.Business,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "",
                )

                Text(
                    text = store.nameDisplay,
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.onPrimary,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    imageVector = Icons.Filled.LocationOn,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "",
                )

                Text(
                    text = store.city,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }
    }
}

@Composable
fun UserBox(
    modifier: Modifier = Modifier,
    user: Collaborator = Collaborator(),
    onEnter: (id: String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .border(border = BorderStroke(
                width = SalesAppTheme.dimensions.grid_0_25,
                color = MaterialTheme.colors.primary.copy(alpha = 0.03f),
            ), shape = RoundedCornerShape(5))
            .padding(SalesAppTheme.dimensions.grid_1)
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .width(128.dp)
                .height(128.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
            ,
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.picture)
                .crossfade(true)
                .size(width = 256, height = 256)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            fallback = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
            placeholder = painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = user.nameDisplay.capitalize(Locale.current),
            style = MaterialTheme.typography.subtitle1,
        )

        OutlinedButton(
            modifier = Modifier
                .width(128.dp)
                .padding(6.dp)
                .height(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = { onEnter(user.id) }
        ) {
            Text(text = stringResource(id = R.string.btn_enter))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UserSignIn(
    modifier: Modifier = Modifier,
    user: Collaborator = Collaborator(),

    isAuthenticating: Boolean = false,

    hasLocalSignIn: Boolean = false,
    onSignIn: () -> Unit,
    onLocalSignIn: () -> Unit,

    password: String = "",
    onPasswordChanged: (value: String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
                .data(user.picture)
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
            visible = !isAuthenticating,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            if (hasLocalSignIn) {
                SignIn(
                    password = password,
                    onPasswordChanged = onPasswordChanged,
                    message = stringResource(id = R.string.message_local_sign_in),
                    onSignIn = onLocalSignIn,
                )
            } else {
                SignIn(
                    password = password,
                    onPasswordChanged = onPasswordChanged,
                    message = stringResource(id = R.string.message_regular_sign_in),
                    onSignIn = onSignIn,
                )
            }
        }

        AnimatedVisibility(
            visible = isAuthenticating,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignIn(
    modifier: Modifier = Modifier,
    message: String = "",
    password: String = "",
    hasError: Boolean = false,
    errorMessage: String = "",
    onPasswordChanged: (value: String) -> Unit = {},
    onSignIn: () -> Unit = {},
) {
    Column(
        modifier = Modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(horizontal = 6.dp),
                imageVector = Icons.Filled.Info,
                tint = MaterialTheme.colors.primary,
                contentDescription = ""
            )

            Text(text = message, style = MaterialTheme.typography.body1)
        }

        PeyessPasswordInput(password = password,
            onValueChange = onPasswordChanged,
            isError = hasError,
            errorMessage = errorMessage,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {
                keyboardController?.hide()
                onSignIn()
            }))

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = onSignIn
        ) {
            Text(text = stringResource(id = R.string.btn_enter))
        }
    }
}

@Preview
@Composable
fun PreviewUserBox(modifier: Modifier = Modifier) {
    UserBox(
        user = Collaborator(picture = "https://images.unsplash.com/photo-1574701148212-8518049c7b2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=686&q=80"),
    )
}