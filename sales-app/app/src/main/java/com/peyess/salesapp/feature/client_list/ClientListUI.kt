package com.peyess.salesapp.feature.client_list

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PieChart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.client_list.model.Client
import com.peyess.salesapp.ui.component.action_bar.ClientActions
import com.peyess.salesapp.ui.theme.SalesAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

private val pictureSize = 90.dp
private const val pictureSizePx = 90

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

private val buttonActionSpacerWidth = 8.dp
private val endingSpacerWidth = 16.dp

private val clientActionPadding = 8.dp

private val lazyColumnHeaderBottomSpacer = 16.dp

@Composable
fun ClientListScreenUI(
    modifier: Modifier = Modifier,

    isLoadingClients: Boolean = false,

    clientList: Flow<PagingData<Client>> = emptyFlow(),

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
    onClientPicked: (client: Client) -> Unit = {},
    onEditClient: (client: Client) -> Unit = {},
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
                        modifier = Modifier.padding(horizontal = clientActionPadding),
                        onCreateNewClient = onCreateNewClient,
                        onSearchClient = {  },
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
                        onEditClient = onEditClient,
                        onClientPicked = onClientPicked,
                    )
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
            text = stringResource(id = R.string.no_clients_yet),
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    client: Client = Client(),
    onEditClient: (client: Client) -> Unit = {},
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

        IconButton(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = CircleShape,
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape,
                )
                .size(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = { onEditClient(client) },
        ) {
            Icon(
                imageVector = Icons.Filled.Edit,
                tint = MaterialTheme.colors.primary,
                contentDescription = "",
            )
        }

        Spacer(modifier = Modifier.width(buttonActionSpacerWidth))

        IconButton(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape,
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary,
                    shape = CircleShape,
                )
                .size(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = { onClientPicked(client) },
        ) {
            Icon(
                imageVector = Icons.Filled.Done,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "",
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
            onSearchClient = {},
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
