package com.peyess.salesapp.screen.create_client.basic_info

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.peyess.salesapp.R
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.screen.create_client.basic_info.state.BasicInfoState
import com.peyess.salesapp.screen.create_client.basic_info.state.BasicInfoViewModel
import com.peyess.salesapp.screen.create_client.basic_info.utils.createClientFile
import com.peyess.salesapp.screen.create_client.basic_info.utils.fromReadableSexName
import com.peyess.salesapp.screen.create_client.basic_info.utils.readableSexName
import com.peyess.salesapp.screen.create_client.utils.parseParameters
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.ui.component.date.PeyessDialogDatePicker
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.text_transformation.UserDocumentVisualTransformation
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import timber.log.Timber
import java.time.LocalDate
import java.time.ZonedDateTime

private const val pictureSize = 256

private val defaultSpacerSize = 32.dp

private val pictureBoxSize = 256.dp
private val dividerSpacerSize = 32.dp


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BasicInfoScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: (
        clientId: String,
        createScenario: CreateScenario,
        paymentId: Long,
    ) -> Unit = { _, _, _ -> },
) {
    val viewModel: BasicInfoViewModel = mavericksViewModel()

    parseParameters(
        navController = navHostController,
        onUpdateClientId = viewModel::onClientIdChanged,
        onUpdatePaymentId = viewModel::onPaymentIdChanged,
        onUpdateCreateScenario = viewModel::onCreateScenarioChanged,
    )

    val clientId by viewModel.collectAsState(BasicInfoState::clientId)
    val scenario by viewModel.collectAsState(BasicInfoState::createScenario)
    val paymentId by viewModel.collectAsState(BasicInfoState::paymentId)

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
            onDone(clientId, scenario, paymentId)
        }
    }

    BasicInfoScreenImpl(
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BasicInfoScreenImpl(
    modifier: Modifier = Modifier,

    picture: Uri = Uri.EMPTY,
    onPictureChanged: (Uri) -> Unit = {},

    name: String = "",
    onNameChanged: (String) -> Unit = {},
    onDetectNameError: () -> Unit = {},

    nameDisplay: String = "",
    onNameDisplayChanged: (String) -> Unit = {},
    onDetectNameDisplayError: () -> Unit = {},

    birthday: ZonedDateTime = ZonedDateTime.now(),
    onBirthdayChanged: (LocalDate) -> Unit = {},

    sex: Sex = Sex.Unknown,
    onSexChanged: (Sex) -> Unit = {},

    document: String = "",
    onDocumentChanged: (String) -> Unit = {},
    onDetectDocumentError: () -> Unit = {},

    @StringRes nameErrorId: Int = R.string.empty_string,
    nameHasError: Boolean = false,

    @StringRes nameDisplayErrorId: Int = R.string.empty_string,
    nameDisplayHasError: Boolean = false,

    @StringRes birthdayErrorId: Int = R.string.empty_string,
    birthdayHasError: Boolean = false,

    @StringRes documentErrorId: Int = R.string.empty_string,
    documentHasError: Boolean = false,

    isInputValid: Boolean = false,

    onDone: () -> Unit = {},
) {
    val density = LocalDensity.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val minWidthInputState = remember { MinimumWidthState() }
    val minWidthSelectableState = remember { MinimumWidthState() }

    var nameReceivedFocus by remember { mutableStateOf(false) }
    var nameDisplayReceivedFocus by remember { mutableStateOf(false) }
    var documentReceivedFocus by remember { mutableStateOf(false) }

    val pickSexDialogState = rememberMaterialDialogState()
    PickSexDialog(
        sexes = Sex.options,
        dialogState = pickSexDialogState,
        onSexSelected = {
            onSexChanged(it)
        },
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        ProfilePicture(
            picture = picture,
            onPictureChanged = onPictureChanged,
        )

        Spacer(modifier = Modifier.height(defaultSpacerSize))

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthInputState, density = density)
                .onFocusChanged {
                    nameReceivedFocus = nameReceivedFocus || it.hasFocus

                    if (nameReceivedFocus && !it.hasFocus) {
                        onDetectNameError()
                    }
                },
            value = name,
            onValueChange = onNameChanged,
            isError = nameHasError,
            errorMessage = stringResource(id = nameErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_name_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_name_input)) },
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
                .minimumWidthModifier(state = minWidthInputState, density = density)
                .onFocusChanged {
                    nameDisplayReceivedFocus = nameDisplayReceivedFocus || it.hasFocus

                    if (nameDisplayReceivedFocus && !it.hasFocus) {
                        onDetectNameDisplayError()
                    }
                },
            value = nameDisplay,
            onValueChange = onNameDisplayChanged,
            isError = nameDisplayHasError,
            errorMessage = stringResource(id = nameDisplayErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_name_display_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_name_display_input)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        )

        Spacer(modifier = Modifier.height(dividerSpacerSize))
        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(dividerSpacerSize))

        PeyessOutlinedTextField(
            modifier = Modifier
                .minimumWidthModifier(state = minWidthInputState, density = density)
                .onFocusChanged {
                    documentReceivedFocus = documentReceivedFocus || it.hasFocus

                    if (documentReceivedFocus && !it.hasFocus) {
                        onDetectDocumentError()
                    }
                },
            value = document,
            onValueChange = onDocumentChanged,
            isError = documentHasError,
            errorMessage = stringResource(id = documentErrorId),
            label = { Text(text = stringResource(id = R.string.create_client_document_input)) },
            placeholder = { Text(text = stringResource(id = R.string.create_client_document_input)) },
            visualTransformation = UserDocumentVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() },
            ),
        )

        Spacer(modifier = Modifier.height(defaultSpacerSize))


        SexPicker(
            modifier = Modifier
                .minimumWidthModifier(
                    state = minWidthSelectableState,
                    density = density,
                )
                .clickable { pickSexDialogState.show() },
            dialogState = pickSexDialogState,
            title = stringResource(id = R.string.create_client_sex_input),
            sex = sex,
        )

        Spacer(modifier = Modifier.height(defaultSpacerSize))

        PeyessDialogDatePicker(
            modifier = Modifier.minimumWidthModifier(
                state = minWidthSelectableState,
                density = density,
            ),
            title = stringResource(id = R.string.create_client_birthday_input),
            currDate = birthday.toLocalDate(),
            onSetDate = onBirthdayChanged,
        )

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            canGoNext = true, //isInputValid,
            onNext = onDone,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ProfilePicture(
    modifier: Modifier = Modifier,

    picture: Uri = Uri.EMPTY,
    onPictureChanged: (Uri) -> Unit = {},
) {
    val context = LocalContext.current

    val pictureFile = remember { createClientFile(context) }
    val pictureFileUri = remember {
        FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            pictureFile,
        )
    }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onPictureChanged(pictureFileUri)
            }
        },
    )

    val takePicture = {
        if (cameraPermissionState.status != PermissionStatus.Granted) {
            cameraPermissionState.launchPermissionRequest()
        } else {
            cameraLauncher.launch(pictureFileUri)
        }
    }

    Box(modifier = modifier.size(pictureBoxSize)) {
        AsyncImage(
            modifier = Modifier
                // Clip image to be shaped as a circle
                .matchParentSize()
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .clip(CircleShape)
                .clickable { takePicture() },
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(picture)
                .crossfade(true)
                .size(width = pictureSize, height = pictureSize)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_profile_placeholder),
            fallback = painterResource(id = R.drawable.ic_profile_placeholder),
            placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
        )

        IconButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomEnd)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                    shape = CircleShape,
                )
                .background(color = MaterialTheme.colors.onPrimary, shape = CircleShape),
            onClick = takePicture,
        ) {
            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                tint = MaterialTheme.colors.primary,
                contentDescription = "",
            )
        }
    }
}

@Composable
private fun PickSexDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    sexes: List<Sex?> = emptyList(),
    onSexSelected: (name: Sex) -> Unit = {},
) {
    val sexesList = sexes
        .filterNotNull()
        .map {
            Timber.i("Mapping $it")
            readableSexName(it)
        }

    Timber.i("Available sexes are: $sexesList from $sexes")

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        // TODO: use string resource
        title("Selecione uma das opções abaixo")

        listItems(
            list = sexesList
        ) { _, item ->
            onSexSelected(fromReadableSexName(item))
            dialogState.hide()
        }
    }
}

@Composable
private fun SexPicker(
    modifier: Modifier = Modifier,
    title: String = "",
    enabled: Boolean = true,
    sex: Sex = Sex.Unknown,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.size(8.dp))

        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (enabled) {
                        MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    } else {
                        Color.Gray.copy(alpha = 0.5f)
                    },
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(2.dp)
                .clickable(enabled = enabled) { dialogState.show() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = readableSexName(sex),
                color = if (enabled) {
                    MaterialTheme.colors.primary.copy(alpha = 0.5f)
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            IconButton(
                enabled = enabled,
                onClick = { dialogState.show() }
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "")
            }
        }
    }
}


@Preview
@Composable
private fun BasicInfoScreenPreview() {
    SalesAppTheme {
        BasicInfoScreenImpl(modifier = Modifier.fillMaxSize())
    }
}