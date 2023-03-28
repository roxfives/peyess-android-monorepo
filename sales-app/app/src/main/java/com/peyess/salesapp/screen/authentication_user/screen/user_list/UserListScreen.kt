package com.peyess.salesapp.screen.authentication_user.screen.user_list

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.peyess.salesapp.R
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.screen.authentication_user.screen.user_list.state.UserListState
import com.peyess.salesapp.screen.authentication_user.screen.user_list.state.UserListViewModel
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.extentions.activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun UserListAuthScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
) {
    val viewModel: UserListViewModel = mavericksViewModel()

    val users by viewModel.collectAsState(UserListState::users)
    val store by viewModel.collectAsState(UserListState::store)
    val isLoading by viewModel.collectAsState(UserListState::isLoading)

    val isUpdatingCurrentUser by viewModel.collectAsState(UserListState::updatingCurrentUser)

    val isStoreLoading by viewModel.collectAsState(UserListState::isStoreLoading)

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

    UserAuthScreenImpl(
        modifier = modifier,
        store = store,
        isLoading = isLoading,
        isStoreLoading = isStoreLoading,
        pictureForUser = viewModel::pictureForUser,
        pictureForStore = viewModel::pictureForStore,
        users = users,
        onEnter = {
            viewModel.pickUser(it)
            canNavigate.value = true
        },
    )
}

@Composable
fun UserAuthScreenImpl(
    modifier: Modifier = Modifier,
    store: OpticalStore = OpticalStore(),
    isLoading: Boolean = false,
    isStoreLoading: Boolean = false,
    pictureForUser: suspend (uid: String) -> Uri = { Uri.EMPTY },
    pictureForStore: suspend (storeId: String) -> Uri = { Uri.EMPTY },
    users: List<CollaboratorDocument> = listOf(),
    onEnter: (id: String) -> Unit = {},
) {
    UserGrid(
        modifier = modifier,
        store = store,
        isStoreLoading = isStoreLoading,
        isLoading = isLoading,
        pictureForUser = pictureForUser,
        pictureForStore = pictureForStore,
        users = users,
        onEnter = onEnter,
    )
}

@Composable
fun UserGrid(
    modifier: Modifier = Modifier,
    store: OpticalStore = OpticalStore(),
    isStoreLoading: Boolean = false,
    isLoading: Boolean = false,
    pictureForUser: suspend (uid: String) -> Uri = { Uri.EMPTY },
    pictureForStore: suspend (storeId: String) -> Uri = { Uri.EMPTY },
    users: List<CollaboratorDocument> = listOf(),
    onEnter: (id: String) -> Unit = {},
) {
    if(isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            Header(
                pictureForStore = pictureForStore,
                isStoreLoading = isStoreLoading,
                store = store,
            )

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
                        modifier = Modifier.padding(SalesAppTheme.dimensions.grid_1),
                        pictureForUser = pictureForUser,
                        user = users[it],
                        onEnter = onEnter
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    isStoreLoading: Boolean = false,
    pictureForStore: suspend (storeId: String) -> Uri = { Uri.EMPTY },
    store: OpticalStore = OpticalStore(),
) {
    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(store) {
        coroutineScope.launch(Dispatchers.IO) {
            pictureUri.value = pictureForStore(store.id)
        }
    }

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
                .data(pictureUri.value)
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
                    modifier = Modifier.placeholder(
                        visible = isStoreLoading,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White.copy(alpha = 0.7f),
                        ),
                    ),
                    text = store.nameDisplay.ifEmpty {
                        stringResource(id = R.string.loading_text_long_placeholder)
                    },
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
                    modifier = Modifier.placeholder(
                        visible = isStoreLoading,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White.copy(alpha = 0.7f),
                        ),
                    ),
                    text = store.city.ifEmpty {
                        stringResource(id = R.string.loading_text_medium_placeholder)
                    },
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onPrimary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "",
                )

                Text(
                    modifier = Modifier.placeholder(
                        visible = isStoreLoading,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(4.dp),
                        highlight = PlaceholderHighlight.shimmer(
                            highlightColor = Color.White.copy(alpha = 0.7f),
                        ),
                    ),
                    text = store.type.ifEmpty {
                        stringResource(id = R.string.loading_text_short_placeholder)
                    },
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onPrimary,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current

            Button(onClick = {
                context.activity()?.finishAffinity()
            }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "",
                    )

                    Text(text = stringResource(id = R.string.home_btn_exit))
                }
            }
        }
    }
}

@Composable
fun UserBox(
    modifier: Modifier = Modifier,
    pictureForUser: suspend (uid: String) -> Uri = { Uri.EMPTY },
    user: CollaboratorDocument = CollaboratorDocument(),
    onEnter: (id: String) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(user) {
        coroutineScope.launch(Dispatchers.IO) {
            pictureUri.value = pictureForUser(user.id)
        }
    }

    Column(
        modifier = modifier
            .border(
                border = BorderStroke(
                    width = SalesAppTheme.dimensions.grid_0_25,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.03f),
                ), shape = RoundedCornerShape(5)
            )
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
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUri.value)
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
private fun HeaderPreview() {
    SalesAppTheme {
        Header()
    }
}

@Preview
@Composable
fun PreviewUserBox(modifier: Modifier = Modifier) {
    UserBox(
        user = CollaboratorDocument(),
    )
}