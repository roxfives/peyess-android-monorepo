package com.peyess.salesapp.screen.create_client.address

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.client_data.address.ClientAddress
import com.peyess.salesapp.screen.create_client.address.state.ClientAddressState
import com.peyess.salesapp.screen.create_client.address.state.ClientAddressViewModel
import com.peyess.salesapp.screen.create_client.utils.ParseParameters
import com.peyess.salesapp.navigation.create_client.CreateScenario

@Composable
fun CreateClientAddressScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: (
        clientId: String,
        createScenario: CreateScenario,
        paymentId: Long,
        saleId: String,
        serviceOrder: String,
    ) -> Unit = { _, _, _, _, _ -> },
) {
    val viewModel: ClientAddressViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateClientId = viewModel::onClientIdChanged,
        onUpdatePaymentId = viewModel::onPaymentIdChanged,
        onUpdateCreateScenario = viewModel::onCreateScenarioChanged,
        onUpdateSaleId = viewModel::onSaleIdChanged,
        onUpdateServiceOrderId = viewModel::onServiceOrderIdChanged,
    )

    val saleId by viewModel.collectAsState(ClientAddressState::saleId)
    val serviceOrderId by viewModel.collectAsState(ClientAddressState::serviceOrderId)

    val clientId by viewModel.collectAsState(ClientAddressState::clientId)
    val paymentId by viewModel.collectAsState(ClientAddressState::paymentId)
    val scenario by viewModel.collectAsState(ClientAddressState::createScenario)

    val zipCode by viewModel.collectAsState(ClientAddressState::zipCodeInput)
    val street by viewModel.collectAsState(ClientAddressState::streetInput)
    val houseNumber by viewModel.collectAsState(ClientAddressState::houseNumberInput)
    val complement by viewModel.collectAsState(ClientAddressState::complementInput)
    val neighbourhood by viewModel.collectAsState(ClientAddressState::neighborhoodInput)
    val city by viewModel.collectAsState(ClientAddressState::cityInput)
    val state by viewModel.collectAsState(ClientAddressState::stateInput)

    val isAddressLoading by viewModel.collectAsState(ClientAddressState::isAddressLoading)
    val isAddressEnabled by viewModel.collectAsState(ClientAddressState::isAddressEnabled)
    val addressNotFound by viewModel.collectAsState(ClientAddressState::addressNotFound)

    val zipCodeErrorId by viewModel.collectAsState(ClientAddressState::zipCodeErrorId)
    val zipCodeHasError by viewModel.collectAsState(ClientAddressState::zipCodeHasError)

    val streetErrorId by viewModel.collectAsState(ClientAddressState::streetErrorId)
    val streetHasError by viewModel.collectAsState(ClientAddressState::streetHasError)

    val houseNumberErrorId by viewModel.collectAsState(ClientAddressState::houseNumberErrorId)
    val houseNumberHasError by viewModel.collectAsState(ClientAddressState::houseNumberHasError)

    val neighbourhoodErrorId by viewModel.collectAsState(ClientAddressState::neighbourhoodErrorId)
    val neighbourhoodHasError by viewModel.collectAsState(ClientAddressState::neighbourhoodHasError)

    val cityErrorId by viewModel.collectAsState(ClientAddressState::cityErrorId)
    val cityHasError by viewModel.collectAsState(ClientAddressState::cityHasError)

    val stateErrorId by viewModel.collectAsState(ClientAddressState::stateErrorId)
    val stateHasError by viewModel.collectAsState(ClientAddressState::stateHasError)

    val isInputValid by viewModel.collectAsState(ClientAddressState::isInputValid)

    val hasFinishedSettingAddress
        by viewModel.collectAsState(ClientAddressState::hasFinishedSettingAddress)

    if (hasFinishedSettingAddress) {
        LaunchedEffect(Unit) {
            viewModel.onNavigate()

            onDone(
                clientId,
                scenario,
                paymentId,
                saleId,
                serviceOrderId,
            )
        }
    }

    ClientAddress(
        modifier = modifier,

        isAddressLoading = isAddressLoading,
        isAddressEnabled = isAddressEnabled,
        addressNotFound = addressNotFound,

        zipCode = zipCode,
        onZipCodeChanged = viewModel::onZipCodeChanged,
        onDetectZipCodeError = viewModel::onDetectZipCodeError,

        street = street,
        onStreetChanged = viewModel::onStreetChanged,
        onDetectStreetError = viewModel::onDetectStreetError,

        houseNumber = houseNumber,
        onHouseNumberChanged = viewModel::onHouseNumberChanged,
        onDetectHouseNumberError = viewModel::onDetectHouseNumberError,

        complement = complement,
        onComplementChanged = viewModel::onComplementChanged,

        neighbourhood = neighbourhood,
        onNeighbourhoodChanged = viewModel::onNeighbourhoodChanged,
        onDetectNeighbourhoodError = viewModel::onDetectNeighbourhoodError,

        city = city,
        onCityChanged = viewModel::onCityChanged,
        onDetectCityError = viewModel::onDetectCityError,

        state = state,
        onStateChanged = viewModel::onStateChanged,
        onDetectStateError = viewModel::onDetectStateError,

        zipCodeErrorId = zipCodeErrorId,
        zipCodeHasError = zipCodeHasError,

        streetErrorId = streetErrorId,
        streetHasError = streetHasError,

        houseNumberErrorId = houseNumberErrorId,
        houseNumberHasError = houseNumberHasError,

        neighbourhoodErrorId = neighbourhoodErrorId,
        neighbourhoodHasError = neighbourhoodHasError,

        cityErrorId = cityErrorId,
        cityHasError = cityHasError,

        stateErrorId = stateErrorId,
        stateHasError = stateHasError,

        isInputValid = isInputValid,
        onDone = viewModel::onFinish,
    )
}
