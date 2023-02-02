package com.peyess.salesapp.feature.sale.pick_client

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.feature.home.dialog.ExistingClientDialog
import com.peyess.salesapp.feature.sale.pick_client.model.Client
import com.peyess.salesapp.feature.sale.pick_client.state.PickClientState
import com.peyess.salesapp.feature.sale.pick_client.state.PickClientViewModel
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.navigation.pick_client.pickScenarioParam
import com.peyess.salesapp.ui.component.action_bar.ClientActions
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber

private val pictureSize = 90.dp
private val pictureSizePx = 90

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

private val endingSpacerWidth = profilePicPadding

private val clientActionPadding = 8.dp

private val lazyColumnHeaderBottomSpacer = 16.dp

@Composable
fun PickClientScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),

    onCreateNewClient: (
        clientId: String,
        paymentId: Long,
        pickScenario: PickScenario,
    ) -> Unit = { _, _, _ -> },

    onSearchClient: () -> Unit = {},

    onClientPicked: (
        paymentId: Long,
        pickedId: String,
        pickScenario: PickScenario,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _, _, _ -> },
) {
    val viewModel: PickClientViewModel = mavericksViewModel()

    val saleId by viewModel.collectAsState(PickClientState::saleId)
    val serviceOrderId by viewModel.collectAsState(PickClientState::serviceOrderId)

    val clientList by viewModel.collectAsState(PickClientState::clientListStream)
    val isLoading by viewModel.collectAsState(PickClientState::isLoading)
    val hasPickedClient by viewModel.collectAsState(PickClientState::hasPickedClient)

    val pickScenario by viewModel.collectAsState(PickClientState::pickScenario)
    val pickedId by viewModel.collectAsState(PickClientState::pickedId)

    val scenarioParameter = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getString(pickScenarioParam)

    val paymentId = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getLong(paymentIdParam)

    LaunchedEffect(scenarioParameter) {
        val scenario = PickScenario.fromName(scenarioParameter ?: "")

        Timber.i("Using scenario $scenario")

        if (scenario != null) {
            viewModel.updatePickScenario(scenario)
        }
    }

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasPickedClient) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                viewModel.clientPicked()
                onClientPicked(
                    paymentId ?: 0L,
                    pickedId,
                    pickScenario,
                    saleId,
                    serviceOrderId,
                )
            }
        }
    }

    val createClientId by viewModel.collectAsState(PickClientState::createClientId)
    val createClient by viewModel.collectAsState(PickClientState::createClient)
    val creatingClientExists by viewModel.collectAsState(PickClientState::creatingClientExists)
    val hasLookedForExistingClient by viewModel.collectAsState(PickClientState::hasLookedForExistingClient)

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
            onCreateNewClient(createClientId, paymentId ?: 0L, pickScenario)
        }
    }

    if (isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            PickClientScreenImpl(
                modifier = modifier,
                clientList = clientList,

                pictureForClient = viewModel::pictureForClient,

                onSearchClient = onSearchClient,
                onCreateNewClient = viewModel::findActiveCreatingClient,
                onClientPicked = {
                    viewModel.pickClient(it)
                    canNavigate.value = true
                },
            )
        }
    }
}

@Composable
private fun PickClientScreenImpl(
    modifier: Modifier = Modifier,
    clientList: Flow<PagingData<Client>> = emptyFlow(),

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
    onClientPicked: (client: Client) -> Unit = {},
    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    val clients = clientList.collectAsLazyPagingItems()

    if (clients.itemCount == 0) {
        NoClientsYet(
            modifier = modifier,
            onCreateNewClient = onCreateNewClient,
            onSearchClient = onSearchClient,
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(spacingBetweenCards),
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ClientActions(
                        modifier = Modifier.padding(horizontal = clientActionPadding),
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
                        onClientPicked = onClientPicked,
                    )
                }
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

                Text(modifier = Modifier.padding(vertical = 4.dp), text = client.shortAddress)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = { onClientPicked(client) },
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(id = R.string.btn_select_client),
            )
        }

        Spacer(modifier = Modifier.width(endingSpacerWidth))
    }
}

@Composable
private fun NoClientsYet(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_no_search_results))

        Row(modifier = Modifier.fillMaxWidth()) {
            ClientActions(
                modifier = Modifier.padding(horizontal = clientActionPadding),
                onCreateNewClient = onCreateNewClient,
                onSearchClient = onSearchClient,
            )

            Spacer(modifier = Modifier.height(lazyColumnHeaderBottomSpacer))
        }

        LottieAnimation(
            modifier = modifier.padding(36.dp),
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

@Preview
@Composable
private fun ClientCardPreview() {
    SalesAppTheme {
        ClientCard(
            modifier = Modifier.fillMaxWidth(),
            client = Client(
                name = "João Ferreira",
            )
        )
    }
}
