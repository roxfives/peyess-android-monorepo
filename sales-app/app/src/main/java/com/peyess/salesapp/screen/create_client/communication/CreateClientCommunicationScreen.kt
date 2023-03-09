package com.peyess.salesapp.screen.create_client.communication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.client_data.communication.ClientCommunication
import com.peyess.salesapp.screen.create_client.communication.state.CommunicationState
import com.peyess.salesapp.screen.create_client.communication.state.CommunicationViewModel
import com.peyess.salesapp.screen.create_client.utils.ParseParameters
import com.peyess.salesapp.navigation.create_client.CreateScenario

@Composable
fun CreateClientCommunicationScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: (
        createScenario: CreateScenario,
        clientId: String,
        paymentId: Long,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _ , _, _-> },
) {
    val viewModel: CommunicationViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateClientId = viewModel::onClientIdChanged,
        onUpdatePaymentId = viewModel::onPaymentIdChanged,
        onUpdateCreateScenario = viewModel::onCreateScenarioChanged,
        onUpdateSaleId = viewModel::onSaleIdChanged,
        onUpdateServiceOrderId = viewModel::onServiceOrderIdChanged,
    )

    val saleId by viewModel.collectAsState(CommunicationState::saleId)
    val serviceOrderId by viewModel.collectAsState(CommunicationState::serviceOrderId)

    val clientId by viewModel.collectAsState(CommunicationState::clientId)
    val paymentId by viewModel.collectAsState(CommunicationState::paymentId)
    val createScenario by viewModel.collectAsState(CommunicationState::createScenario)

    val hasAcceptedPromotionalMessages by
        viewModel.collectAsState(CommunicationState::hasAcceptedPromotionalMessages)

    val email by viewModel.collectAsState(CommunicationState::emailInput)
    val cellphone by viewModel.collectAsState(CommunicationState::cellphoneInput)
    val whatsapp by viewModel.collectAsState(CommunicationState::whatsappInput)
    val phone by viewModel.collectAsState(CommunicationState::phoneInput)

    val phoneHasWhatsApp by viewModel.collectAsState(CommunicationState::phoneHasWhatsApp)
    val hasPhoneContact by viewModel.collectAsState(CommunicationState::hasPhoneContact)

    val emailErrorId by viewModel.collectAsState(CommunicationState::emailErrorId)
    val emailHasError by viewModel.collectAsState(CommunicationState::emailHasError)

    val cellphoneErrorId by viewModel.collectAsState(CommunicationState::cellphoneErrorId)
    val cellphoneHasError by viewModel.collectAsState(CommunicationState::cellphoneHasError)

    val whatsappErrorId by viewModel.collectAsState(CommunicationState::whatsappErrorId)
    val whatsappHasError by viewModel.collectAsState(CommunicationState::whatsappHasError)

    val phoneErrorId by viewModel.collectAsState(CommunicationState::phoneErrorId)
    val phoneHasError by viewModel.collectAsState(CommunicationState::phoneHasError)

    val isInputValid by viewModel.collectAsState(CommunicationState::isInputValid)

    val clientCreated by viewModel.collectAsState(CommunicationState::clientCreated)
    val isUploadingClient by viewModel.collectAsState(CommunicationState::isUploadingClient)

    val hasFinishedSettingCommunication
        by viewModel.collectAsState(CommunicationState::hasFinishedSettingCommunication)

    if (hasFinishedSettingCommunication) {
        LaunchedEffect(Unit) {
            viewModel.createClient()
        }
    }

    if (clientCreated) {
        viewModel.onNavigate()

        LaunchedEffect(Unit) {
            onDone(
                createScenario,
                clientId,
                paymentId,
                saleId,
                serviceOrderId,
            )
        }
    }

    ClientCommunication(
        modifier = modifier,

        isUploadingClient = isUploadingClient,

        phoneHasWhatsApp = phoneHasWhatsApp,
        onPhoneHasWhatsAppChanged = viewModel::onPhoneHasWhatsappChanged,

        hasPhoneContact = hasPhoneContact,
        onHasPhoneChanged = viewModel::onHasPhoneChanged,

        hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
        onHasAcceptedPromotionalMessages = viewModel::onHasAcceptedPromotionalMessages,

        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        onDetectEmailError = viewModel::onDetectEmailError,

        cellphone = cellphone,
        onCellphoneChanged = viewModel::onCellphoneChanged,
        onDetectCellphoneError = viewModel::onDetectCellphoneError,

        whatsapp = whatsapp,
        onWhatsappChanged = viewModel::onWhatsappChanged,
        onDetectWhatsappError = viewModel::onDetectWhatsappError,

        phone = phone,
        onPhoneChanged = viewModel::onPhoneChanged,
        onDetectPhoneError = viewModel::onDetectPhoneError,

        emailErrorId = emailErrorId,
        emailHasError = emailHasError,

        cellphoneErrorId = cellphoneErrorId,
        cellphoneHasError = cellphoneHasError,

        whatsappErrorId = whatsappErrorId,
        whatsappHasError = whatsappHasError,

        phoneErrorId = phoneErrorId,
        phoneHasError = phoneHasError,

        isInputValid = isInputValid,
        onDone = viewModel::onFinishSettingCommunication,
    )
}
