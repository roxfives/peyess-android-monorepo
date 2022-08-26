package com.peyess.salesapp.feature.sale.pick_client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.peyess.salesapp.dao.client.firestore.Client
import com.peyess.salesapp.feature.sale.pick_client.state.PickClientState
import com.peyess.salesapp.feature.sale.pick_client.state.PickClientViewModel
import com.peyess.salesapp.feature.sale.prescription_data.betweenSectionSpacer
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val buttonHeight = 72.dp

private val pictureSize = 90.dp
private val pictureSizePx = 90

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

private val endingSpacerWidth = profilePicPadding

@Composable
fun PickClientScreen(
    modifier: Modifier = Modifier,
    onClientPicked: () -> Unit = {},
) {
    val viewModel: PickClientViewModel = mavericksViewModel()

    val clients by viewModel.collectAsState(PickClientState::clientList)
    val isLoading by viewModel.collectAsState(PickClientState::isLoading)
    val hasPickedClient by viewModel.collectAsState(PickClientState::hasPickedClient)

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasPickedClient) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                viewModel.clientPicked()
                onClientPicked()
            }
        }
    }

    if (isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            PickClientScreenImpl(
                modifier = modifier,
                clients = clients,
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
    clients: List<Client> = emptyList(),
    onClientPicked: (client: Client) -> Unit = {}
) {
    if (clients.isEmpty()) {
        NoClientsYet(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(spacingBetweenCards),
        ) {
            item {
                OutlinedButton(
                    modifier = Modifier
                        .padding(horizontal = SalesAppTheme.dimensions.grid_1_5)
                        .fillMaxWidth()
                        .height(buttonHeight),
                    shape = MaterialTheme.shapes.large,
                    onClick = {},
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(buttonHeight)
                                .width(buttonHeight)
                                .padding(horizontal = SalesAppTheme.dimensions.grid_1)
                                .background(color = MaterialTheme.colors.primary.copy(alpha = 0.5f)),
                        ) {
                            Icon(
                                modifier = Modifier.align(Alignment.Center),
                                imageVector = Icons.Filled.PersonAdd,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = "",
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.btn_add_new_client),
                            style = MaterialTheme.typography.body1,
                        )
                    }
                }
            }

            items(clients.size) {
                val client = clients[it]

                ClientCard(
                    modifier = Modifier.fillMaxWidth(),
                    client = client,
                    onClientPicked = onClientPicked,
                )
            }
        }
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    client: Client = Client(),
    onClientPicked: (client: Client) -> Unit = {},
) {
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
                .data(client.picture)
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
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_no_search_results))

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
                shortAddress = "Guarulhos, SP",
                picture = "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
            )
        )
    }
}