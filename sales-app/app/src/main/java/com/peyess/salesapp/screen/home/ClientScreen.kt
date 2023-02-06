package com.peyess.salesapp.screen.home

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.app.model.Client
import com.peyess.salesapp.app.state.ClientListStream
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.screen.home.dialog.ExistingClientDialog
import com.peyess.salesapp.ui.component.action_bar.ClientActions
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber

private val lazyColumnHeaderBottomSpacer = 16.dp

private val pictureSize = 90.dp
private val pictureSizePx = 90

private val noClientsHeight = 120.dp

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

// TODO: Refactor this component to remove duplicated code (PickClientScreen)

@Composable
fun ClientScreen(
    modifier: Modifier = Modifier,

    onCreateNewClient: (clientId: String) -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    val context = LocalContext.current

    val viewModel: MainViewModel = mavericksActivityViewModel()

    val createClientId by viewModel.collectAsState(MainAppState::createClientId)
    val createClient by viewModel.collectAsState(MainAppState::createClient)
    val creatingClientExists by viewModel.collectAsState(MainAppState::creatingClientExists)
    val hasLookedForExistingClient by viewModel.collectAsState(MainAppState::hasLookedForExistingClient)

    val isLoading by viewModel.collectAsState(MainAppState::areClientsLoading)
    val clientList by viewModel.collectAsState(MainAppState::clientListStream)

    val createClientDialogState = rememberMaterialDialogState()
    ExistingClientDialog(
        dialogState = createClientDialogState,
        onCreateNewClient = viewModel::createNewClient,
        onUseExistingClient = viewModel::createClientFromCache,
    )

    if (hasLookedForExistingClient && creatingClientExists) {
        LaunchedEffect(Unit) {
            viewModel.checkedForExistingClient()
            createClientDialogState.show()
        }
    }

    if (hasLookedForExistingClient && !creatingClientExists) {
        LaunchedEffect(Unit) {
            viewModel.createNewClient()
        }
    }

    if (createClient) {
        LaunchedEffect(Unit) {
            viewModel.startedCreatingClient()
            Timber.i("Creating client with id: $createClientId")
            onCreateNewClient(createClientId)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        ClientScreenImpl(
            modifier = modifier,

            isLoadingClients = isLoading,
            pictureForClient = viewModel::pictureForClient,
            clientList = clientList,

            onCreateNewClient = viewModel::findActiveCreatingClient,
            onSearchClient = onSearchClient,
            onSyncClients = {
                viewModel.syncClients(context)
            },
        )
    }
}

@Composable
private fun ClientScreenImpl(
    modifier: Modifier = Modifier,

    clientList: ClientListStream = emptyFlow(),
    isLoadingClients: Boolean = false,

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},

    onSyncClients: () -> Unit = {},
) {
    val clients = clientList.collectAsLazyPagingItems()

    if (isLoadingClients) {
        LoadingClients(modifier)
    } else if (clients.itemCount == 0) {
        NoClientsYet(
            modifier = modifier,

            onCreateNewClient = onCreateNewClient,
            onSearchClient = onSearchClient,

            syncClients = onSyncClients,
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(spacingBetweenCards),
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ClientActions(
                        onCreateNewClient = onCreateNewClient,
                        onSearchClient = onSearchClient,
                    )

                    Spacer(modifier = Modifier.height(lazyColumnHeaderBottomSpacer))
                }
            }

            items(clients.itemCount) { index ->
                val client = clients[index]

                client?.let {
                    ClientCard(
                        modifier = Modifier.fillMaxWidth(),
                        pictureForClient = pictureForClient,
                        client = it,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    client: Client = Client(),
    onClientPicked: (client: Client) -> Unit = {},
    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
) {
    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val picture = pictureForClient(client.id)

            pictureUri.value = picture
        }
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(profilePicPadding)
                .size(pictureSize)
                // Clip image to be shaped as a circle
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .clip(CircleShape),
            model = ImageRequest.Builder(LocalContext.current)
                .data(pictureUri.value)
                .crossfade(true)
                .size(width = pictureSizePx, height = pictureSizePx)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_profile_placeholder),
            fallback = painterResource(id = R.drawable.ic_profile_placeholder),
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
        )

        Spacer(modifier = Modifier.width(cardSpacerWidth))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(cardPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = client.name,
                style = MaterialTheme.typography.body1
                    .copy(fontWeight = FontWeight.Bold)
            )

            Row(
                Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    modifier = Modifier.fillMaxHeight(),
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "",
                )

                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = client.shortAddress,
                )
            }
        }
    }
}

@Composable
private fun LoadingClients(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_loading)
        )

        LottieAnimation(
            modifier = modifier
                .padding(36.dp)
                .fillMaxWidth(),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            text = stringResource(id = R.string.no_clients_yet),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun NoClientsYet(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
    syncClients: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_no_data)
        )

        ClientActions(
            modifier = Modifier.padding(horizontal = 8.dp),
            onCreateNewClient = onCreateNewClient,
            onSearchClient = onSearchClient,
        )

        LottieAnimation(
            modifier = Modifier
                .padding(36.dp)
                .height(360.dp)
                .width(420.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            text = stringResource(id = R.string.no_clients_yet),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(onClick = syncClients) {
            Text(text = stringResource(id = R.string.clients_screen_btn_sync))
        }
    }
}
