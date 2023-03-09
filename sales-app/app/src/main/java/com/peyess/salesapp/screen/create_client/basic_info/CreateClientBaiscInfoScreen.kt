package com.peyess.salesapp.screen.create_client.basic_info

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.client_data.basic_info.ClientBasicInfo
import com.peyess.salesapp.screen.create_client.basic_info.state.BasicInfoState
import com.peyess.salesapp.screen.create_client.basic_info.state.BasicInfoViewModel
import com.peyess.salesapp.screen.create_client.utils.ParseParameters
import com.peyess.salesapp.navigation.create_client.CreateScenario

@Composable
fun BasicInfoScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: (
        clientId: String,
        createScenario: CreateScenario,
        paymentId: Long,
        saleId: String,
        serviceOrderId: String,
        isUpdatingAnExistingClient: Boolean,
    ) -> Unit = { _, _, _, _, _, _ -> },
) {
    val viewModel: BasicInfoViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateClientId = viewModel::onClientIdChanged,
        onUpdatePaymentId = viewModel::onPaymentIdChanged,
        onUpdateCreateScenario = viewModel::onCreateScenarioChanged,
        onUpdateSaleId = viewModel::onSaleIdChanged,
        onUpdateServiceOrderId = viewModel::onServiceOrderIdChanged,
        onUpdateExistingClient = viewModel::onUpdateExistingClientChanged,
    )

    val saleId by viewModel.collectAsState(BasicInfoState::saleId)
    val serviceOrderId by viewModel.collectAsState(BasicInfoState::serviceOrderId)

    val clientId by viewModel.collectAsState(BasicInfoState::clientId)
    val scenario by viewModel.collectAsState(BasicInfoState::createScenario)
    val paymentId by viewModel.collectAsState(BasicInfoState::paymentId)
    val isUpdatingExistingClient by viewModel
        .collectAsState(BasicInfoState::isUpdatingAnExistingClient)

    val name by viewModel.collectAsState(BasicInfoState::nameInput)
    val nameDisplay by viewModel.collectAsState(BasicInfoState::nameDisplayInput)
    val picture by viewModel.collectAsState(BasicInfoState::pictureInput)
    val birthday by viewModel.collectAsState(BasicInfoState::birthdayInput)
    val document by viewModel.collectAsState(BasicInfoState::documentInput)
    val sex by viewModel.collectAsState(BasicInfoState::sexInput)

    val nameErrorId by viewModel.collectAsState(BasicInfoState::nameErrorId)
    val nameHasError by viewModel.collectAsState(BasicInfoState::nameHasError)

    val nameDisplayErrorId by viewModel.collectAsState(BasicInfoState::nameDisplayErrorId)
    val nameDisplayHasError by viewModel.collectAsState(BasicInfoState::nameDisplayHasError)

    val birthdayErrorId by viewModel.collectAsState(BasicInfoState::birthdayErrorId)
    val birthdayHasError by viewModel.collectAsState(BasicInfoState::birthdayHasError)

    val documentErrorId by viewModel.collectAsState(BasicInfoState::documentErrorId)
    val documentHasError by viewModel.collectAsState(BasicInfoState::documentHasError)

    val isInputValid by viewModel.collectAsState(BasicInfoState::isInputValid)

    val hasFinishedSettingBasicInfo
        by viewModel.collectAsState(BasicInfoState::hasFinishedSettingBasicInfo)
    if (hasFinishedSettingBasicInfo) {
        LaunchedEffect(Unit) {
            viewModel.onNavigate()

            onDone(
                clientId,
                scenario,
                paymentId,
                saleId,
                serviceOrderId,
                isUpdatingExistingClient,
            )
        }
    }

    ClientBasicInfo(
        modifier = modifier,

        picture = picture,
        onPictureChanged = viewModel::onPictureChanged,

        name = name,
        onNameChanged = viewModel::onNameChanged,
        onDetectNameError = viewModel::onDetectNameError,

        nameDisplay = nameDisplay,
        onNameDisplayChanged = viewModel::onNameDisplayChanged,
        onDetectNameDisplayError = viewModel::onDetectNameDisplayError,

        birthday = birthday,
        onBirthdayChanged = viewModel::onBirthdayChanged,

        sex = sex,
        onSexChanged = viewModel::onSexChanged,

        document = document,
        onDocumentChanged = viewModel::onDocumentChanged,
        onDetectDocumentError = viewModel::onDetectDocumentError,

        nameErrorId = nameErrorId,
        nameHasError = nameHasError,

        nameDisplayErrorId = nameDisplayErrorId,
        nameDisplayHasError = nameDisplayHasError,

        birthdayErrorId = birthdayErrorId,
        birthdayHasError = birthdayHasError,

        documentErrorId = documentErrorId,
        documentHasError = documentHasError,
        isInputValid = isInputValid,

        onDone = viewModel::onFinishBasicInfo,
    )
}
