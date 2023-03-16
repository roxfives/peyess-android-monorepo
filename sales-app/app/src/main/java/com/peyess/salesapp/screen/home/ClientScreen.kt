package com.peyess.salesapp.screen.home

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
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
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.ui.component.action_bar.ClientActions
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.ZonedDateTime

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
    onEditClient: (clientId: String) -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    val context = LocalContext.current

    val viewModel: MainViewModel = mavericksActivityViewModel()

    val createClientId by viewModel.collectAsState(MainAppState::createClientId)
    val createClient by viewModel.collectAsState(MainAppState::createClient)
    val creatingClientExists by viewModel.collectAsState(MainAppState::creatingClientExists)
    val hasLookedForExistingClient by viewModel.collectAsState(MainAppState::hasLookedForExistingClient)

    val updateClientId by viewModel.collectAsState(MainAppState::updateClientId)
    val updateClient by viewModel.collectAsState(MainAppState::updateClient)

    val isLoading by viewModel.collectAsState(MainAppState::areClientsLoading)
    val clientList by viewModel.collectAsState(MainAppState::clientListStream)

    val clientSearchQuery by viewModel.collectAsState(MainAppState::clientSearchQuery)

    val isSearchActive by viewModel.collectAsState(MainAppState::isSearchActive)
    val clientListSearchStream by viewModel.collectAsState(MainAppState::clientListSearchStream)
    val isLoadingClientSearch by viewModel.collectAsState(MainAppState::isLoadingClientSearch)

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

    if (updateClient) {
        LaunchedEffect(Unit) {
            viewModel.startedUpdatingClient()
            Timber.i("Updating client with id: $createClientId")
            onEditClient(updateClientId)
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
            onEditClient = { viewModel.loadEditClientToCache(it.id) },

            isSearchActive = isSearchActive,
            clientSearchList = clientListSearchStream,
            isLoadingClientSearch = isLoadingClientSearch,
            clientSearchQuery = clientSearchQuery,
            onSearchClient = viewModel::onClientSearchChanged,
            onClearClientSearch = viewModel::clearClientSearch,
            onStartSearching = viewModel::startClientSearch,
            onStopSearching = viewModel::stopClientSearch,

            onSyncClients = { viewModel.syncClients(context) },
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
    onEditClient: (Client) -> Unit = {},

    clientSearchList: ClientListStream = emptyFlow(),
    isLoadingClientSearch: Boolean = false,
    clientSearchQuery: String = "",
    isSearchActive: Boolean = false,
    onSearchClient: (query: String) -> Unit = {},
    onClearClientSearch: () -> Unit = {},
    onStartSearching: () -> Unit = {},
    onStopSearching: () -> Unit = {},

    onSyncClients: () -> Unit = {},
) {
    ClientList(
        modifier = modifier,

        clientList = if (isSearchActive) clientSearchList else clientList,
        isLoadingClients = if (isSearchActive) isLoadingClientSearch else isLoadingClients,

        pictureForClient = pictureForClient,
        onCreateNewClient = onCreateNewClient,
        onEditClient = onEditClient,

        clientSearchQuery = clientSearchQuery,
        isSearchActive = isSearchActive,
        onSearchClient = onSearchClient,
        onClearClientSearch = onClearClientSearch,
        onStartSearching = onStartSearching,
        onStopSearching = onStopSearching,

        onSyncClients = onSyncClients,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ClientList(
    modifier: Modifier = Modifier,

    clientList: ClientListStream = emptyFlow(),
    isLoadingClients: Boolean = false,

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
    onCreateNewClient: () -> Unit = {},
    onEditClient: (Client) -> Unit = {},
    canEditClient: Boolean = true,

    clientSearchQuery: String = "",
    isSearchActive: Boolean = false,
    onSearchClient: (query: String) -> Unit = {},
    onClearClientSearch: () -> Unit = {},
    onStartSearching: () -> Unit = {},
    onStopSearching: () -> Unit = {},

    onSyncClients: () -> Unit = {},
) {
    val clients = clientList.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingBetweenCards),
    ) {
        stickyHeader {
            Surface(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ClientActions(
                        onCreateNewClient = onCreateNewClient,

                        clientSearchQuery = clientSearchQuery,
                        onSearchClient = onSearchClient,

                        isSearchActive = isSearchActive,
                        onStartSearching = onStartSearching,
                        onStopSearching = onStopSearching,
                    )

                    Spacer(modifier = Modifier.height(lazyColumnHeaderBottomSpacer))
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = !isSearchActive
                        && (isLoadingClients || clients.loadState.refresh is LoadState.Loading),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                LoadingClients(modifier)
            }

            AnimatedVisibility(
                visible = !isLoadingClients
                        && !isSearchActive
                        && clients.loadState.refresh is LoadState.NotLoading
                        && clients.itemCount == 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                NoClientsYet(
                    modifier = modifier,
                    syncClients = onSyncClients,
                )
            }

            AnimatedVisibility(
                visible = !isLoadingClients
                        && isSearchActive
                        && clients.loadState.refresh is LoadState.NotLoading
                        && clients.itemCount == 0,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                NoClientsFound(
                    modifier = modifier,
                    onClearSearch = onClearClientSearch,
                )
            }
        }

        items(clients.itemCount) { index ->
            val client = clients[index]

            client?.let {
                ClientCard(
                    modifier = Modifier.fillMaxWidth(),
                    pictureForClient = pictureForClient,
                    client = it,
                    onEditClient = onEditClient,
                    canEditClient = canEditClient,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    client: Client = Client(),
    onClientPicked: (client: Client) -> Unit = {},
    onEditClient: (client: Client) -> Unit = {},
    canEditClient: Boolean = true,
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

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
            )
            .padding(cardPadding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.Top,
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

//        Spacer(modifier = Modifier.height(cardSpacerWidth))

        AnimatedVisibility(
            visible = canEditClient,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
            ) {
                Button(onClick = { onEditClient(client) }) {
                    Text(text = stringResource(id = R.string.clients_screen_btn_edit))
                }
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
            text = stringResource(id = R.string.searching_clients),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun NoClientsYet(
    modifier: Modifier = Modifier,

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

@Composable
private fun NoClientsFound(
    modifier: Modifier = Modifier,
    onClearSearch: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_no_search_results)
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
            text = stringResource(id = R.string.no_clients_found),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(onClick = onClearSearch) {
            Text(text = stringResource(id = R.string.clients_screen_btn_clear_search))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ClientCardPreview() {
    SalesAppTheme {
        ClientCard(
            client = Client(
                id = "",
                name = "",
                nameDisplay = "",
                birthday = ZonedDateTime.now(),
                document = "",
                sex = Sex.Unknown,
                shortAddress = "",
                zipCode = "",
                street = "",
                houseNumber = "",
                complement = "",
                neighborhood = "",
                city = "",
                state = "",
                email = "",
                phone = "",
                cellphone = "",
                whatsapp = ""
            ),

            onClientPicked = {},
            pictureForClient = { Uri.EMPTY },
        )
    }
}
