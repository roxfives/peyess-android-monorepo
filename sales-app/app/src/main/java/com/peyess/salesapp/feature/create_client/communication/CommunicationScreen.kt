package com.peyess.salesapp.feature.create_client.communication

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.create_client.communication.state.CommunicationState
import com.peyess.salesapp.feature.create_client.communication.state.CommunicationViewModel
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.createScenarioParam
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.text_transformation.CellphoneNumberVisualTransformation
import com.peyess.salesapp.ui.text_transformation.PhoneNumberVisualTransformation
import timber.log.Timber

private val defaultSpacerSize = 32.dp
private val checkboxSpacerWidth = 4.dp
private val checkboxSpacerHeight = 16.dp

@Composable
fun CreateClientCommunicationScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    viewModelScope: LifecycleOwner? = null,
    onDone: (
        createScenario: CreateScenario,
        clientId: String,
        paymentId: Long,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _ , _, _-> },
) {
    val viewModel: CommunicationViewModel = if (viewModelScope == null) {
        mavericksViewModel()
    } else {
        mavericksViewModel(viewModelScope)
    }

    val saleId by viewModel.collectAsState(CommunicationState::saleId)
    val serviceOrderId by viewModel.collectAsState(CommunicationState::serviceOrderId)

    val createScenarioParam = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getString(createScenarioParam)
        ?: CreateScenario.Home.toName()
    val paymentId = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getLong(paymentIdParam)
        ?: 0L

    LaunchedEffect(createScenarioParam, paymentId) {
        viewModel.updatePickScenario(createScenarioParam, paymentId)
    }

    var scenario by remember { mutableStateOf<CreateScenario>(CreateScenario.Home) }
    val scenarioParameter = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getString(com.peyess.salesapp.navigation.create_client.createScenarioParam)

    LaunchedEffect(scenarioParameter) {
        scenario = CreateScenario.fromName(scenarioParameter ?: "") ?: CreateScenario.Home

        Timber.i("Using scenario $scenario")
    }

    val isUploadingClient by viewModel.collectAsState(CommunicationState::isUploadingClient)
    val uploadSuccessful by viewModel.collectAsState(CommunicationState::uploadSuccessful)

    val clientId by viewModel.collectAsState(CommunicationState::uploadedId)

    LaunchedEffect(uploadSuccessful, clientId) {
        if (uploadSuccessful && clientId.isNotBlank()) {
            Timber.i("Navigating with client $clientId")

            onDone(scenario, clientId, paymentId, saleId, serviceOrderId)
        }
    }

    val hasAcceptedPromotionalMessages by
        viewModel.collectAsState(CommunicationState::hasAcceptedPromotionalMessages)

    val email by viewModel.collectAsState(CommunicationState::email)
    val cellphone by viewModel.collectAsState(CommunicationState::cellphone)
    val whatsapp by viewModel.collectAsState(CommunicationState::whatsapp)
    val phone by viewModel.collectAsState(CommunicationState::phone)

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

    CommunicationScreenImpl(
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
        onDone = viewModel::createClient,
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
private fun CommunicationScreenImpl(
    modifier: Modifier = Modifier,

    isUploadingClient: Boolean = true,

    phoneHasWhatsApp: Boolean = true,
    onPhoneHasWhatsAppChanged: (Boolean) -> Unit = {},

    hasPhoneContact: Boolean = false,
    onHasPhoneChanged: (Boolean) -> Unit = {},

    hasAcceptedPromotionalMessages: Boolean = false,
    onHasAcceptedPromotionalMessages: (Boolean) -> Unit = {},

    email: String = "",
    onEmailChanged: (String) -> Unit = {},
    onDetectEmailError: () -> Unit = {},

    cellphone: String = "",
    onCellphoneChanged: (String) -> Unit = {},
    onDetectCellphoneError: () -> Unit = {},

    whatsapp: String = "",
    onWhatsappChanged: (String) -> Unit = {},
    onDetectWhatsappError: () -> Unit = {},

    phone: String = "",
    onPhoneChanged: (String) -> Unit = {},
    onDetectPhoneError: () -> Unit = {},

    @StringRes emailErrorId: Int = R.string.empty_string,
    emailHasError: Boolean = false,

    @StringRes cellphoneErrorId: Int = R.string.empty_string,
    cellphoneHasError: Boolean = false,

    @StringRes whatsappErrorId: Int = R.string.empty_string,
    whatsappHasError: Boolean = false,

    @StringRes phoneErrorId: Int = R.string.empty_string,
    phoneHasError: Boolean = false,

    isInputValid: Boolean = false,
    onDone: () -> Unit = {},
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val inputMinWidthState = remember { MinimumWidthState() }
    val checkboxMinWidthState = remember { MinimumWidthState() }

    var emailReceivedFocus by remember { mutableStateOf(false) }
    var cellphoneReceivedFocus by remember { mutableStateOf(false) }
    var whatsappReceivedFocus by remember { mutableStateOf(false) }
    var phoneReceivedFocus by remember { mutableStateOf(false) }

    if (isUploadingClient) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.lottie_loading)
        )

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f)
        )
    } else {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(defaultSpacerSize))

            Text(
                text = stringResource(id = R.string.create_client_communication_title),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.height(defaultSpacerSize))

            PeyessOutlinedTextField(
                modifier = Modifier
                    .minimumWidthModifier(state = inputMinWidthState, density = density)
                    .onFocusChanged {
                        emailReceivedFocus = emailReceivedFocus || it.hasFocus

                        if (emailReceivedFocus && !it.hasFocus) {
                            onDetectEmailError()
                        }
                    },
                value = email,
                onValueChange = onEmailChanged,
                isError = emailHasError,
                errorMessage = stringResource(id = emailErrorId),
                label = { Text(text = stringResource(id = R.string.create_client_email_input)) },
                placeholder = { Text(text = stringResource(id = R.string.create_client_email_input)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
            )

            PeyessOutlinedTextField(
                modifier = Modifier
                    .minimumWidthModifier(state = inputMinWidthState, density = density)
                    .onFocusChanged {
                        cellphoneReceivedFocus = cellphoneReceivedFocus || it.hasFocus

                        if (cellphoneReceivedFocus && !it.hasFocus) {
                            onDetectCellphoneError()
                        }
                    },
                value = cellphone,
                onValueChange = onCellphoneChanged,
                isError = cellphoneHasError,
                errorMessage = stringResource(id = cellphoneErrorId),
                label = { Text(text = stringResource(id = R.string.create_client_cellphone_input)) },
                placeholder = { Text(text = stringResource(id = R.string.create_client_cellphone_input)) },
                visualTransformation = CellphoneNumberVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (phoneHasWhatsApp && !hasPhoneContact) ImeAction.Done else ImeAction.Next,
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Phone,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = { keyboardController?.hide() }
                ),
            )

            AnimatedVisibility(
                visible = !phoneHasWhatsApp,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                PeyessOutlinedTextField(
                    modifier = Modifier
                        .minimumWidthModifier(state = inputMinWidthState, density = density)
                        .onFocusChanged {
                            whatsappReceivedFocus = whatsappReceivedFocus || it.hasFocus

                            if (whatsappReceivedFocus && !it.hasFocus) {
                                onDetectWhatsappError()
                            }
                        },
                    value = whatsapp,
                    onValueChange = onWhatsappChanged,
                    isError = whatsappHasError,
                    errorMessage = stringResource(id = whatsappErrorId),
                    label = { Text(text = stringResource(id = R.string.create_client_whatsapp_input)) },
                    placeholder = { Text(text = stringResource(id = R.string.create_client_whatsapp_input)) },
                    visualTransformation = CellphoneNumberVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (hasPhoneContact) ImeAction.Next else ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Phone,
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        onDone = { keyboardController?.hide() }
                    ),
                )
            }

            AnimatedVisibility(
                visible = hasPhoneContact,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                PeyessOutlinedTextField(
                    modifier = Modifier
                        .minimumWidthModifier(state = inputMinWidthState, density = density)
                        .onFocusChanged {
                            phoneReceivedFocus = phoneReceivedFocus || it.hasFocus

                            if (phoneReceivedFocus && !it.hasFocus) {
                                onDetectPhoneError()
                            }
                        },
                    value = phone,
                    onValueChange = onPhoneChanged,
                    isError = phoneHasError,
                    errorMessage = stringResource(id = phoneErrorId),
                    label = { Text(text = stringResource(id = R.string.create_client_phone_input)) },
                    placeholder = { Text(text = stringResource(id = R.string.create_client_phone_input)) },
                    visualTransformation = PhoneNumberVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Words,
                        keyboardType = KeyboardType.Phone,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() },
                    ),
                )
            }

            Spacer(modifier = Modifier.height(checkboxSpacerHeight))

            Row(
                modifier = Modifier.minimumWidthModifier(
                    state = checkboxMinWidthState,
                    density = density,
                ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = phoneHasWhatsApp,
                    onCheckedChange = onPhoneHasWhatsAppChanged,
                )

                Spacer(modifier = Modifier.width(checkboxSpacerWidth))

                Text(text = stringResource(id = R.string.create_client_communication_same_phone_and_whatsapp))
            }

            Row(
                modifier = Modifier.minimumWidthModifier(
                    state = checkboxMinWidthState,
                    density = density,
                ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = hasPhoneContact,
                    onCheckedChange = onHasPhoneChanged,
                )

                Spacer(modifier = Modifier.width(checkboxSpacerWidth))

                Text(text = stringResource(id = R.string.create_client_communication_phone_exists))
            }

            Row(
                modifier = Modifier.minimumWidthModifier(
                    state = checkboxMinWidthState,
                    density = density,
                ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = hasAcceptedPromotionalMessages,
                    onCheckedChange = onHasAcceptedPromotionalMessages,
                )

                Spacer(modifier = Modifier.width(checkboxSpacerWidth))

                Text(text = stringResource(id = R.string.create_client_accept_promotional_messages))
            }

            Spacer(modifier = Modifier.weight(1f))

            PeyessStepperFooter(
                canGoNext = isInputValid,
                onNext = onDone,
            )
        }
    }
}