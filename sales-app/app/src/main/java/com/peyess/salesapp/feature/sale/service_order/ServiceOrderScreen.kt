package com.peyess.salesapp.feature.sale.service_order

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
//import com.google.accompanist.placeholder.placeholder
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
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.placeholder.material.placeholder
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderState
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderViewModel
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val pictureSize = 60.dp
private val pictureSizePx = 60

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val spacingBetweenCards = 8.dp
private val profilePicPadding = 8.dp

private val endingSpacerWidth = profilePicPadding

private val sectionPadding = 16.dp
private val subsectionSpacerSize = 16.dp

@Composable
fun ServiceOrderScreen(
    modifier: Modifier = Modifier,

    onChangeClient: (client: ClientEntity) -> Unit = {},
    onChangeLens: () -> Unit = {},
    onChangeFrames: () -> Unit = {},
    onConfirmMeasure: () -> Unit = {},

    onGenerateBudget: () -> Unit = {},
    onFinishSale: () -> Unit = {},
) {
    val viewModel: ServiceOrderViewModel = mavericksViewModel()

    val user by viewModel.collectAsState(ServiceOrderState::userClient)
    val responsible by viewModel.collectAsState(ServiceOrderState::responsibleClient)
    val witness by viewModel.collectAsState(ServiceOrderState::witnessClient)

    val userIsLoading by viewModel.collectAsState(ServiceOrderState::isUserLoading)
    val responsibleIsLoading by viewModel.collectAsState(ServiceOrderState::isResponsibleLoading)
    val witnessIsLoading by viewModel.collectAsState(ServiceOrderState::isWitnessLoading)

    ServiceOrderScreenImpl(
        modifier = modifier,

        onFinishSale = {},

        areUsersLoading = userIsLoading || responsibleIsLoading || witnessIsLoading,
        user = user,
        responsible = responsible,
        witness = witness,
    )
}

@Composable
private fun ServiceOrderScreenImpl(
    modifier: Modifier = Modifier,

    onFinishSale: () -> Unit = {},

    areUsersLoading: Boolean = false,
    user: ClientEntity = ClientEntity(),
    responsible: ClientEntity = ClientEntity(),
    witness: ClientEntity? = null,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        ClientSection(
            isLoading = areUsersLoading,

            user = user,
            responsible = responsible,
            witness = witness,
        )
    }
}

@Composable
private fun ClientSection(
    modifier: Modifier = Modifier,

    isLoading: Boolean = false,

    user: ClientEntity = ClientEntity(),
    responsible: ClientEntity = ClientEntity(),
    witness: ClientEntity? = null,
) {
    Column(
        modifier = modifier
            .padding(sectionPadding),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SectionTitle(
                title = stringResource(id = R.string.so_section_title_clients)
            )

            Spacer(modifier = Modifier.weight(1f))

            IconButton(
                onClick = { /*TODO*/ },
                enabled = !isLoading,
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.size(subsectionSpacerSize))

        Column(modifier = Modifier.padding(subsectionSpacerSize)) {
            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_user)
            )

            ClientCard(client = user, isLoading = isLoading)

            Spacer(modifier = Modifier.height(16.dp))

            SubSectionTitle(
                title = stringResource(id = R.string.so_subsection_title_responsible)
            )

            ClientCard(client = responsible, isLoading = isLoading)

            if (witness != null) {
                Spacer(modifier = Modifier.height(16.dp))

                SubSectionTitle(
                    title = stringResource(id = R.string.so_subsection_title_witness)
                )

                ClientCard(client = user, isLoading = isLoading)
            }
        }
    }
}

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
    )
}

@Composable
fun SubSectionTitle(
    modifier: Modifier = Modifier,
    title: String = "",
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
    )
}

@Preview
@Composable
fun SectionTitlePreview() {
    SalesAppTheme {
        SectionTitle(title = stringResource(id = R.string.so_section_title_clients))
    }
}

@Preview
@Composable
fun SubSectionTitlePreview() {
    SalesAppTheme {
        SubSectionTitle(title = stringResource(id = R.string.so_subsection_title_frames))
    }
}

@Composable
private fun ClientCard(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    client: ClientEntity,
    onEditClient: (role: ClientRole) -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
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
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .placeholder(visible = isLoading),
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
                        modifier = Modifier
                            .fillMaxHeight()
                            .placeholder(
                                visible = isLoading
                            ),
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "",
                    )

                    Text(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .placeholder(
                                visible = isLoading
                            ),
                        text = client.shortAddress,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                enabled = !isLoading,
                onClick = { onEditClient(client.clientRole) },
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(id = R.string.btn_change_client),
                )
            }

            Spacer(modifier = Modifier.width(endingSpacerWidth))
        }
    }
}

@Preview
@Composable
private fun ClientSectionPreview() {
    SalesAppTheme {
        ClientSection(
            user = ClientEntity(name = "João da Silva", shortAddress = "São Paulo, SP"),
            responsible = ClientEntity(name = "João da Silva", shortAddress = "São Paulo, SP"),
        )
    }
}