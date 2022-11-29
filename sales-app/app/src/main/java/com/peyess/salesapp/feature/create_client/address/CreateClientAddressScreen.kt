package com.peyess.salesapp.feature.create_client.address

import androidx.annotation.StringRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.layout.ContentScale
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
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.create_client.address.state.ClientAddressState
import com.peyess.salesapp.feature.create_client.address.state.ClientAddressViewModel
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.createScenarioParam
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.pick_client.isPickingParam
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.navigation.pick_client.pickScenarioParam
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.text_transformation.UserZipCodeVisualTransformation
import timber.log.Timber

private val defaultSpacerSize = 32.dp

private val warningSpacerSize = 8.dp

private val animationSpacerHeight = 16.dp
private val animationSize = 124.dp

private val animationDuration = 2000
private val animationProgressStart = 0f
private val animationProgressEnd = 1f

@Composable
fun CreateClientAddressScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    viewModelScope: LifecycleOwner? = null,
    onDone: (
        createScenario: CreateScenario,
        paymentId: Long,
    ) -> Unit = { _, _-> },
) {
    val viewModel: ClientAddressViewModel = if (viewModelScope == null) {
        mavericksViewModel()
    } else {
        mavericksViewModel(viewModelScope)
    }

    val isPicking = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getBoolean(isPickingParam)
        ?: false

    val pickScenarioParam = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getString(pickScenarioParam)
        ?: PickScenario.ServiceOrder.toName()
    val paymentId = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getLong(paymentIdParam)
        ?: 0L

    var scenario by remember { mutableStateOf<CreateScenario>(CreateScenario.Home) }
    val scenarioParameter = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getString(createScenarioParam)

    LaunchedEffect(scenarioParameter) {
        scenario = CreateScenario.fromName(scenarioParameter ?: "") ?: CreateScenario.Home

        Timber.i("Using scenario $scenario")
    }

    val zipCode by viewModel.collectAsState(ClientAddressState::zipCode)
    val street by viewModel.collectAsState(ClientAddressState::street)
    val houseNumber by viewModel.collectAsState(ClientAddressState::houseNumber)
    val complement by viewModel.collectAsState(ClientAddressState::complement)
    val neighbourhood by viewModel.collectAsState(ClientAddressState::neighborhood)
    val city by viewModel.collectAsState(ClientAddressState::city)
    val state by viewModel.collectAsState(ClientAddressState::state)

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

    CreateClientAddressScreenImpl(
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
        onDone = { onDone(scenario, paymentId) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CreateClientAddressScreenImpl(
    modifier: Modifier = Modifier,

    isAddressLoading: Boolean = false,
    isAddressEnabled: Boolean = false,
    addressNotFound: Boolean = false,

    zipCode: String = "",
    onZipCodeChanged: (String) -> Unit = {},
    onDetectZipCodeError: () -> Unit = {},

    street: String = "",
    onStreetChanged: (String) -> Unit = {},
    onDetectStreetError: () -> Unit = {},

    houseNumber: String = "",
    onHouseNumberChanged: (String) -> Unit = {},
    onDetectHouseNumberError: () -> Unit = {},

    complement: String = "",
    onComplementChanged: (String) -> Unit = {},

    neighbourhood: String = "",
    onNeighbourhoodChanged: (String) -> Unit = {},
    onDetectNeighbourhoodError: () -> Unit = {},

    city: String = "",
    onCityChanged: (String) -> Unit = {},
    onDetectCityError: () -> Unit = {},

    state: String = "",
    onStateChanged: (String) -> Unit = {},
    onDetectStateError: () -> Unit = {},

    @StringRes zipCodeErrorId: Int = R.string.empty_string,
    zipCodeHasError: Boolean = false,

    @StringRes streetErrorId: Int = R.string.empty_string,
    streetHasError: Boolean = false,

    @StringRes houseNumberErrorId: Int = R.string.empty_string,
    houseNumberHasError: Boolean = false,

    @StringRes neighbourhoodErrorId: Int = R.string.empty_string,
    neighbourhoodHasError: Boolean = false,

    @StringRes cityErrorId: Int = R.string.empty_string,
    cityHasError: Boolean = false,

    @StringRes stateErrorId: Int = R.string.empty_string,
    stateHasError: Boolean = false,

    isInputValid: Boolean = false,
    onDone: () -> Unit = {},
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val minWidthState = remember { MinimumWidthState() }

    var zipCodeReceivedFocus by remember { mutableStateOf(false) }
    var streetReceivedFocus by remember { mutableStateOf(false) }
    var houseNumberReceivedFocus by remember { mutableStateOf(false) }
    var neighbourhoodReceivedFocus by remember { mutableStateOf(false) }
    var cityReceivedFocus by remember { mutableStateOf(false) }
    var stateReceivedFocus by remember { mutableStateOf(false) }

    @StringRes
    val instructionsId = if (isAddressLoading) {
        R.string.create_client_loading_address
    } else {
        R.string.create_client_type_address
    }

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.lottie_search_location) 
    )
    var isPlaying by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (isPlaying) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = if (isPlaying) {
            tween(
                durationMillis = animationDuration,
                easing = LinearEasing,
            )
        } else {
            tween(easing = LinearEasing)
        },
    ) {
        isPlaying = false
    }

    LaunchedEffect(isAddressLoading) {
        isPlaying = isAddressLoading && !isPlaying
    }
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(defaultSpacerSize))

        LottieAnimation(
            modifier = Modifier.size(animationSize),
            composition = composition,
            progress = { progress },
            contentScale = ContentScale.FillBounds,
            clipToCompositionBounds = false,

        )

        Spacer(modifier = Modifier.height(animationSpacerHeight))

        if (addressNotFound) {
            Row {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    tint = MaterialTheme.colors.error,
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(warningSpacerSize))

                Text(
                    text = stringResource(id = R.string.create_client_address_not_found),
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                )
            }
        } else {
            Text(
                text = stringResource(id = instructionsId),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            )
        }

        Spacer(modifier = Modifier.height(defaultSpacerSize))

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    zipCodeReceivedFocus = zipCodeReceivedFocus || it.hasFocus

                    if (zipCodeReceivedFocus && !it.hasFocus) {
                        onDetectZipCodeError()
                    }
                },
            value = zipCode,
            onValueChange = onZipCodeChanged,
            isError = zipCodeHasError,
            errorMessage = stringResource(id = zipCodeErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_zip_code_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_zip_code_input)) },
            visualTransformation = UserZipCodeVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    streetReceivedFocus = streetReceivedFocus || it.hasFocus

                    if (streetReceivedFocus && !it.hasFocus) {
                        onDetectStreetError()
                    }
                },
            value = street,
            onValueChange = onStreetChanged,
            enabled = isAddressEnabled,
            isError = streetHasError,
            errorMessage = stringResource(id = streetErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_street_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_street_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    houseNumberReceivedFocus = houseNumberReceivedFocus || it.hasFocus

                    if (houseNumberReceivedFocus && !it.hasFocus) {
                        onDetectHouseNumberError()
                    }
                },
            value = houseNumber,
            onValueChange = onHouseNumberChanged,
            enabled = isAddressEnabled,
            isError = houseNumberHasError,
            errorMessage = stringResource(id = houseNumberErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_house_number_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_house_number_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density),
            value = complement,
            onValueChange = onComplementChanged,
            enabled = isAddressEnabled,
            isError = false,
            errorMessage = "",
            label = { Text(text = stringResource(id = R.string.create_client_complement_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_complement_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    neighbourhoodReceivedFocus = neighbourhoodReceivedFocus || it.hasFocus

                    if (neighbourhoodReceivedFocus && !it.hasFocus) {
                        onDetectNeighbourhoodError()
                    }
                },
            value = neighbourhood,
            onValueChange = onNeighbourhoodChanged,
            enabled = isAddressEnabled,
            isError = neighbourhoodHasError,
            errorMessage = stringResource(id = neighbourhoodErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_neighbourhood_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_neighbourhood_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    cityReceivedFocus = cityReceivedFocus || it.hasFocus

                    if (cityReceivedFocus && !it.hasFocus) {
                        onDetectCityError()
                    }
                },
            value = city,
            onValueChange = onCityChanged,
            enabled = isAddressEnabled,
            isError = cityHasError,
            errorMessage = stringResource(id = cityErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_city_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_city_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthState, density = density)
                .onFocusChanged {
                    stateReceivedFocus = stateReceivedFocus || it.hasFocus

                    if (stateReceivedFocus && !it.hasFocus) {
                        onDetectStateError()
                    }
                },
            value = state,
            onValueChange = onStateChanged,
            enabled = isAddressEnabled,
            isError = stateHasError,
            errorMessage = stringResource(id = stateErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_state_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_state_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() },
            ),
        )

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            canGoNext = isInputValid,
            onNext = onDone,
        )
    }
}