package com.peyess.salesapp.screen.sale.pick_client

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.client_list.ClientListScreenUI
import com.peyess.salesapp.screen.home.dialog.ExistingClientDialog
import com.peyess.salesapp.screen.sale.pick_client.state.PickClientState
import com.peyess.salesapp.screen.sale.pick_client.state.PickClientViewModel
import com.peyess.salesapp.screen.sale.pick_client.utils.ParseParameters
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import timber.log.Timber

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
        clientId: String,
        pickScenario: PickScenario,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _, _, _ -> },
) {
    val context = LocalContext.current

    val viewModel: PickClientViewModel = mavericksViewModel()

    val saleId by viewModel.collectAsState(PickClientState::saleId)
    val serviceOrderId by viewModel.collectAsState(PickClientState::serviceOrderId)

    val clientList by viewModel.collectAsState(PickClientState::clientListStream)
    val isLoading by viewModel.collectAsState(PickClientState::isLoading)
    val hasPickedClient by viewModel.collectAsState(PickClientState::hasPickedClient)

    val paymentId by viewModel.collectAsState(PickClientState::paymentId)
    val pickScenario by viewModel.collectAsState(PickClientState::pickScenario)
    val pickedId by viewModel.collectAsState(PickClientState::pickedId)

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::setSaleId,
        onUpdateServiceOrderId = viewModel::setServiceOrderId,
        onUpdatePaymentId = viewModel::updatePaymentId,
        onUpdateCreateScenario = viewModel::updatePickScenario,
    )

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasPickedClient) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                viewModel.clientPicked()
                onClientPicked(
                    paymentId,
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
            onCreateNewClient(createClientId, paymentId, pickScenario)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        ClientListScreenUI(
            modifier = modifier,

            isLoadingClients = isLoading,

            clientList = clientList,

            pictureForClient = viewModel::pictureForClient,

            onSyncClients = { viewModel.syncClients(context) },
            onSearchClient = onSearchClient,
            onCreateNewClient = viewModel::findActiveCreatingClient,
            onClientPicked = {
                viewModel.pickClient(it)
                canNavigate.value = true
            },
        )
    }
}
