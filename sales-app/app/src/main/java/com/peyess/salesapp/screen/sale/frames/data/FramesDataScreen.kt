package com.peyess.salesapp.screen.sale.frames.data

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.frames.data.state.FramesDataState
import com.peyess.salesapp.screen.sale.frames.data.state.FramesDataViewModel
import com.peyess.salesapp.screen.sale.frames.data.utils.parseParameters
import com.peyess.salesapp.typing.frames.FramesType
import com.peyess.salesapp.ui.component.chip.PeyessChipGroup
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.component.text.utils.currencyDigitsOnlyOrEmpty
import com.peyess.salesapp.ui.text_transformation.CurrencyVisualTransformation
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun FramesDataScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: () -> Unit = {},
) {
    val viewModel: FramesDataViewModel = mavericksViewModel()

    LaunchedEffect(Unit) {
        parseParameters(
            backStackEntry = navHostController.currentBackStackEntry,
            onUpdateServiceId = viewModel::onUpdateServiceOrder,
        )
    }

    val areFramesNew by viewModel.collectAsState(FramesDataState::areFramesNewInput)

    val info by viewModel.collectAsState(FramesDataState::infoInput)

    val design by viewModel.collectAsState(FramesDataState::designInput)
    val reference by viewModel.collectAsState(FramesDataState::referenceInput)
    val value by viewModel.collectAsState(FramesDataState::valueInput)
    val tagCode by viewModel.collectAsState(FramesDataState::tagCodeInput)
    val framesType by viewModel.collectAsState(FramesDataState::framesTypeInput)

    val mikeMessage by viewModel.collectAsState(FramesDataState::mikeMessage)
    val showMike by viewModel.collectAsState(FramesDataState::showMike)

    val hasFinished by viewModel.collectAsState(FramesDataState::finishedSettingFrames)

    val isUpdatingFrames by viewModel.collectAsState(FramesDataState::isUpdatingFrames)

    if (hasFinished) {
        LaunchedEffect(Unit) {
            onDone()
        }
    }

    FramesDataScreenImpl(
        modifier = modifier,
        onDone = viewModel::onFinishSettingFrames,
        onCancel = viewModel::onCancel,

        areFramesNew = areFramesNew,

        info = info,
        onInfoChange = viewModel::onFramesInfoChanged,

        description = design,
        onDescriptionChange = viewModel::onFramesDescriptionChanged,

        reference = reference,
        onReferenceChange = viewModel::onFramesReferenceChanged,

        value = value,
        onValueChange = viewModel::onFramesValueChanged,

        tagCode = tagCode,
        onTagCodeChange = viewModel::onFramesTagCodeChanged,

        mikeMessageBadFramesType = mikeMessage,
        showMike = showMike,
        framesType = framesType,
        onFramesTypeChange = viewModel::onFramesTypeChanged,

        isUpdatingFrames = isUpdatingFrames,
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
private fun FramesDataScreenImpl(
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
    onCancel: () -> Unit = {},

    areFramesNew: Boolean = true,
    canSetFrames: Boolean = true,

    infoHasError: Boolean = false,
    infoErrorMessage: String = "",
    info: String = "",
    onInfoChange: (value: String) -> Unit = {},

    descriptionHasError: Boolean = false,
    descriptionErrorMessage: String = "",
    description: String = "",
    onDescriptionChange: (value: String) -> Unit = {},

    referenceHasError: Boolean = false,
    referenceErrorMessage: String = "",
    reference: String = "",
    onReferenceChange: (value: String) -> Unit = {},

    valueHasError: Boolean = false,
    valueErrorMessage: String = "",
    value: BigDecimal = BigDecimal.ZERO,
    onValueChange: (value: BigDecimal) -> Unit = {},

    tagCodeHasError: Boolean = false,
    tagCodeErrorMessage: String = "",
    tagCode: String = "",
    onTagCodeChange: (value: String) -> Unit = {},

    showMike: Boolean = false,
    mikeMessageBadFramesType: String = "",
    framesType: FramesType? = null,
    onFramesTypeChange: (value: String) -> Unit = {},

    isUpdatingFrames: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val curPriceInput = value.setScale(2, RoundingMode.HALF_EVEN)
//        .multiply(BigDecimal(100))
//        .toBigInteger()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.frames_type),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
        )
        PeyessChipGroup(
            keepSameWidth = false,
            items = FramesType.listOfPositions,
            itemName = { FramesType.toName(it ?: FramesType.None) },
            selected = framesType,
            onSelectedChanged = onFramesTypeChange,
        )
        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = showMike,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            MikeBubbleRight(
                modifier = Modifier
                    .height(256.dp)
                    .padding(horizontal = 16.dp),
                text = mikeMessageBadFramesType,
            )


            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (areFramesNew) {
            Text(
                text = stringResource(id = R.string.frames_data),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            )
        } else {
            Text(
                text = stringResource(id = R.string.own_frames_data),
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (!areFramesNew) {
            PeyessOutlinedTextField(
                value = info,
                onValueChange =  onInfoChange,
                isError = infoHasError,
                errorMessage = infoErrorMessage,
                label = { Text(stringResource(id = R.string.frames_info)) },
                placeholder = { Text(stringResource(id = R.string.frames_info)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
            )
        }

        if (areFramesNew) {
            PeyessOutlinedTextField(
                value = description,
                onValueChange =  onDescriptionChange,
                isError = descriptionHasError,
                errorMessage = descriptionErrorMessage,
                label = { Text(stringResource(id = R.string.frames_description)) },
                placeholder = { Text(stringResource(id = R.string.frames_description)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                ),
            )

            PeyessOutlinedTextField(
                value = reference,
                onValueChange =  onReferenceChange,
                isError = referenceHasError,
                errorMessage = referenceErrorMessage,
                label = { Text(stringResource(id = R.string.frames_reference)) },
                placeholder = { Text(stringResource(id = R.string.frames_reference)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                ),
            )

            PeyessOutlinedTextField(
                value = currencyDigitsOnlyOrEmpty(curPriceInput),
                onValueChange = {
                    val value = try {
                        BigDecimal(it)
                            .setScale(2, RoundingMode.DOWN)
                            .divide(BigDecimal("100"))
                    } catch (t: Throwable) {
                        Timber.e(t, "Failed to parse $it")
                        BigDecimal.ZERO
                    }

                    onValueChange(value)
                },
                isError = valueHasError,
                errorMessage = valueErrorMessage,
                label = { Text(stringResource(id = R.string.frames_value)) },
                placeholder = { Text(stringResource(id = R.string.frames_value)) },
                visualTransformation = CurrencyVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }
                ),
            )

            PeyessOutlinedTextField(
                value = tagCode,
                onValueChange = onTagCodeChange,
                isError = tagCodeHasError,
                errorMessage = tagCodeErrorMessage,
                label = { Text(stringResource(id = R.string.frames_tag_code)) },
                placeholder = { Text(stringResource(id = R.string.frames_tag_code)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        PeyessStepperFooter(
            startButton = {
                OutlinedButton(
                    modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error,
                        disabledBackgroundColor = Color.Gray.copy(alpha = 0.5f),
                    ),
                ) {
                    Text(text = stringResource(id = R.string.btn_cancel_frames))
                }
            },

            isLoadingConstraints = isUpdatingFrames,
            canGoNext = canSetFrames,
            onNext = onDone,
        )
    }
}

@Preview
@Composable
fun SetFramesScreenPreview() {
    SalesAppTheme {
        FramesDataScreenImpl(
            modifier = Modifier.fillMaxSize(),

            descriptionHasError = false,
            descriptionErrorMessage = "",
            description = "",
            onDescriptionChange = {},

            referenceHasError = false,
            referenceErrorMessage = "",
            reference = "",
            onReferenceChange = {},

            valueHasError = false,
            valueErrorMessage = "",
            value = BigDecimal("11122"),
            onValueChange = {},

            tagCodeHasError = false,
            tagCodeErrorMessage = "",
            tagCode = "",
            onTagCodeChange = {},

            showMike = true,
            framesType = FramesType.MetalEnclosed,
            onFramesTypeChange = {},
        )
    }
}