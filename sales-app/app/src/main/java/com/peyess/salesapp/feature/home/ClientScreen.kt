package com.peyess.salesapp.feature.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme

// TODO: Refactor this component to remove duplicated code (PickClientScreen)
private val clientSearchSpacer = 16.dp
private val clientSearchRoundedCornerShape = 32.dp

private val lazyColumnHeaderBottomSpacer = 16.dp

private val pictureSize = 90.dp
private val pictureSizePx = 90

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

@Composable
fun ClientScreen(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    val viewModel: MainViewModel = mavericksActivityViewModel()

    val isLoading by viewModel.collectAsState(MainAppState::areClientsLoading)
    val clients by viewModel.collectAsState(MainAppState::clientList)

    if (isLoading) {
        PeyessProgressIndicatorInfinite()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))

            ClientScreenImpl(
                modifier = modifier,

                clients = clients,

                onCreateNewClient = onCreateNewClient,
                onSearchClient = onSearchClient,
            )
        }
    }
}

@Composable
private fun ClientScreenImpl(
    modifier: Modifier = Modifier,

    clients: List<ClientDocument> = emptyList(),

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    if (clients.isEmpty()) {
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
                        onCreateNewClient = onCreateNewClient,
                        onSearchClient = onSearchClient,
                    )

                    Spacer(modifier = Modifier.height(lazyColumnHeaderBottomSpacer))
                }
            }

            items(clients.size) {
                val client = clients[it]

                ClientCard(
                    modifier = Modifier.fillMaxWidth(),
                    client = client,
                    onClientPicked = {},
                )
            }
        }
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    client: ClientDocument = ClientDocument(),
    onClientPicked: (client: ClientDocument) -> Unit = {},
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

//        Spacer(modifier = Modifier.weight(1f))

//        Button(
//            modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
//            onClick = { onClientPicked(client) },
//        ) {
//            Text(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                text = stringResource(id = R.string.btn_select_client),
//            )
//        }
//
//        Spacer(modifier = Modifier.width(endingSpacerWidth))
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

        ClientActions(
            modifier = Modifier.padding(horizontal = 8.dp),
            onCreateNewClient = onCreateNewClient,
            onSearchClient = onSearchClient,
        )

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

@Composable
fun ClientActions(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .weight(1f),
            shape = RoundedCornerShape(clientSearchRoundedCornerShape),
            onClick = onSearchClient,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "")

                Spacer(modifier = Modifier.width(clientSearchSpacer))

                Text(text = stringResource(id = R.string.btn_client_action_search_client))
            }
        }

        Spacer(modifier = Modifier.width(clientSearchSpacer))

        IconButton(
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = MaterialTheme.colors.primary,
                )
//                .border(
//                    border = BorderStroke(
//                        width = clientAddButtonBorderWidth,
//                        color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
//                    ),
//                    shape = CircleShape,
//                )
                .size(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = onCreateNewClient,
        ) {
            Icon(
                imageVector = Icons.Filled.PersonAdd,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
fun ClientActionsPreview(
    modifier: Modifier = Modifier,
) {
    SalesAppTheme {
        ClientActions()
    }
}