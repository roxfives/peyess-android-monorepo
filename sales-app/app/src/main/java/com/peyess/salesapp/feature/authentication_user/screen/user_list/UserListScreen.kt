package com.peyess.salesapp.feature.authentication_user.screen.user_list

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
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
import com.peyess.salesapp.feature.authentication_user.screen.user_list.state.UserListState
import com.peyess.salesapp.feature.authentication_user.screen.user_list.state.UserListViewModel
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val viewModel: UserListViewModel = mavericksViewModel()

    val users by viewModel.collectAsState(UserListState::users)
    val store by viewModel.collectAsState(UserListState::currentStore)

    val isUpdatingCurrentUser by viewModel.collectAsState(UserListState::updatingCurrentUser)

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && isUpdatingCurrentUser is Success && !isUpdatingCurrentUser.invoke()!!) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                navHostController.navigate(SalesAppScreens.UserAuth.name)
            }
        }
    }

    UserAuthScreenComposable(
        modifier = modifier,
        store = store,
        users = users,
        onEnter = {
            viewModel.pickUser(it)
            canNavigate.value = true
        },
    )
}

@Composable
fun UserAuthScreenComposable(
    modifier: Modifier = Modifier,
    store: Async<OpticalStore> = Uninitialized,

    users: List<Collaborator> = listOf(),
    onEnter: (id: String) -> Unit = {},
) {
    UserGrid(
        modifier = modifier,
        store = store,
        users = users,

        onEnter = onEnter,
    )
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
                columns = GridCells.Adaptive(minSize = 256.dp),
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
            error = painterResource(id = R.drawable.ic_default_placeholder),
            fallback = painterResource(id = R.drawable.ic_default_placeholder),
            placeholder = painterResource(id = R.drawable.ic_default_placeholder),
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
            error = painterResource(id = R.drawable.ic_profile_placeholder),
            fallback = painterResource(id = R.drawable.ic_profile_placeholder),
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
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

@Preview
@Composable
fun PreviewUserBox(modifier: Modifier = Modifier) {
    UserBox(
        user = Collaborator(picture = "https://images.unsplash.com/photo-1574701148212-8518049c7b2c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=686&q=80"),
    )
}