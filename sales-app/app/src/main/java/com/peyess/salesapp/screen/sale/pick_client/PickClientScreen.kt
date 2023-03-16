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
import com.peyess.salesapp.app.state.MainAppState
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

    onEditClient: (clientId: String) -> Unit = {},
    onCreateNewClient: (
        clientId: String,
        paymentId: Long,
        pickScenario: PickScenario,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _, _, _ -> },

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

    val updateClientId by viewModel.collectAsState(PickClientState::updateClientId)
    val updateClient by viewModel.collectAsState(PickClientState::updateClient)

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


    if (updateClient) {
        LaunchedEffect(Unit) {
            viewModel.startedUpdatingClient()
            Timber.i("Updating client with id: $updateClientId")
            onEditClient(updateClientId)
        }
    }

    val createClientId by viewModel.collectAsState(PickClientState::createClientId)
    val createClient by viewModel.collectAsState(PickClientState::createClient)
    val creatingClientExists by viewModel.collectAsState(PickClientState::creatingClientExists)
    val hasLookedForExistingClient by viewModel.collectAsState(PickClientState::hasLookedForExistingClient)

    val clientSearchQuery by viewModel.collectAsState(PickClientState::clientSearchQuery)

    val isSearchActive by viewModel.collectAsState(PickClientState::isSearchActive)
    val clientListSearchStream by viewModel.collectAsState(PickClientState::clientListSearchStream)
    val isLoadingClientSearch by viewModel.collectAsState(PickClientState::isLoadingClientSearch)


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
            onCreateNewClient(createClientId, paymentId, pickScenario, saleId, serviceOrderId)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        ClientListScreenUI(
            modifier = modifier,

            isLoadingClients = if (isSearchActive) isLoadingClientSearch else isLoading,
            clientList = if (isSearchActive) clientListSearchStream else clientList,

            pictureForClient = viewModel::pictureForClient,

            onEditClient = { viewModel.loadEditClientToCache(it.id) },
            onCreateNewClient = viewModel::findActiveCreatingClient,
            onClientPicked = {
                viewModel.pickClient(it)
                canNavigate.value = true
            },

            isSearchActive = isSearchActive,
            clientSearchQuery = clientSearchQuery,
            onSearchClient = viewModel::onClientSearchChanged,
            onClearClientSearch = viewModel::clearClientSearch,
            onStartSearching = viewModel::startClientSearch,
            onStopSearching = viewModel::stopClientSearch,

            onSyncClients = { viewModel.syncClients(context) },
        )
    }
}
