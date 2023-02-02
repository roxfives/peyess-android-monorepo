package com.peyess.salesapp.feature.sale.prescription_picture

import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.window.DialogProperties
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
import com.peyess.salesapp.feature.sale.prescription_picture.state.PrescriptionPictureState
import com.peyess.salesapp.feature.sale.prescription_picture.state.PrescriptionPictureViewModel
import com.peyess.salesapp.navigation.sale.prescription.isUpdatingParam
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.file.createPrescriptionFile
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val buttonSpacing = 16.dp
private val buttonIconSpacing = 16.dp
private val modalFooterSize = 64.dp

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrescriptionPictureScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onNext: (isUpdating: Boolean) -> Unit = {},
) {
    val isUpdatingParam = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getBoolean(isUpdatingParam)
        ?: false

    val context = LocalContext.current

    val viewModel: PrescriptionPictureViewModel = mavericksViewModel()

    val canGoNext by viewModel.collectAsState(PrescriptionPictureState::canGoNext)
    val isLoading by viewModel.collectAsState(PrescriptionPictureState::isLoading)

    val picture by viewModel.collectAsState(PrescriptionPictureState::pictureUri)
    val date by viewModel.collectAsState(PrescriptionPictureState::prescriptionDate)

    val professionalId by viewModel.collectAsState(PrescriptionPictureState::professionalIdInput)
    val professionalName by viewModel.collectAsState(PrescriptionPictureState::professionalNameInput)

    val pictureFile = remember { createPrescriptionFile(context) }
    val pictureFileUri = remember {
        FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            pictureFile,
        )
    }

    val isCopy by viewModel.collectAsState(PrescriptionPictureState::isCopy)

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                viewModel.onPictureTaken(pictureFileUri)
            }
        }
    )

    val filesPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            android.Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        rememberPermissionState(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    val filesLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                viewModel.onPictureTaken(it)
            }
        }
    )

    if (!isLoading) {
        PrescriptionPictureImpl(
            modifier = modifier,

            canGoNext = canGoNext,

            picture = picture,
            date = date.toLocalDate(),

            isCopy = isCopy,
            onCopyChanged = viewModel::onCopyChanged,

            professionalId = professionalId,
            professionalName = professionalName,

            onProfessionalIdChanged = viewModel::onProfessionalIdChanged,
            onProfessionalNameChanged = viewModel::onProfessionalNameChanged,

            takePicture = {
                if (cameraPermissionState.status == PermissionStatus.Granted) {
                    cameraLauncher.launch(pictureFileUri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },

            pickFromDevice = {
                if (filesPermissionState.status == PermissionStatus.Granted) {
                    filesLauncher.launch("image/*")
                } else {
                    filesPermissionState.launchPermissionRequest()
                }
            },

            onSetDate = viewModel::onDatePicked,
            onNext = {
                if (canGoNext) {
                    onNext(isUpdatingParam)
                }
            },
        )
    } else {
        PeyessProgressIndicatorInfinite()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PrescriptionPictureImpl(
    modifier: Modifier = Modifier,

    canGoNext: Boolean = true,

    isCopy: Boolean = false,
    onCopyChanged: (isCopy: Boolean) -> Unit = {},

    picture: Uri = Uri.EMPTY,
    date: LocalDate = LocalDate.now(),

    professionalId: String = "",
    professionalName: String = "",

    onProfessionalIdChanged: (value: String) -> Unit = {},
    onProfessionalNameChanged: (value: String) -> Unit = {},

    takePicture: () -> Unit = {},
    pickFromDevice: () -> Unit = {},
    onSetDate: (date: LocalDate) -> Unit = {},
    onNext: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true,
    )

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch { bottomSheetState.hide() }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxSize(),
        sheetState = bottomSheetState,
        sheetElevation = 480.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PictureSourceBottomSheetContent(
                modifier = Modifier.fillMaxWidth(),
                onPickCamera = {
                    coroutineScope.launch { bottomSheetState.hide() }
                    takePicture()
                },
                onPickGallery = {
                    coroutineScope.launch { bottomSheetState.hide() }
                    pickFromDevice()
                },
            )
        },
        content = {
            ModelBottomSheetContent(
                modifier = modifier,
                canGoNext = canGoNext,
                isCopy = isCopy,
                onCopyChanged = onCopyChanged,
                picture = picture,
                date = date,
                professionalId = professionalId,
                professionalName = professionalName,
                onProfessionalIdChanged = onProfessionalIdChanged,
                onProfessionalNameChanged = onProfessionalNameChanged,
                choosePictureSource = {
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                },
                onSetDate = onSetDate,
                onNext = onNext,
            )
        }
    )
}

@Composable
private fun PictureSourceBottomSheetContent(
    modifier: Modifier = Modifier,
    onPickCamera: () -> Unit = {},
    onPickGallery: () -> Unit = {},
) {
    Column(modifier = modifier) {
        PickSourceButton(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Camera,
            text = stringResource(id = R.string.btn_take_picture),
            onClick = onPickCamera
        )

        Spacer(modifier = Modifier.height(buttonSpacing))

        PickSourceButton(
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.PhotoLibrary,
            text = stringResource(id = R.string.btn_select_picture),
            onClick = onPickGallery
        )

        Spacer(modifier = Modifier.height(modalFooterSize))
    }
}

@Composable
private fun PickSourceButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon, contentDescription = null
            )

            Spacer(modifier = Modifier.width(buttonIconSpacing))

            Text(text = text)
        }
    }
}

@Composable
private fun ModelBottomSheetContent(
    modifier: Modifier = Modifier,

    canGoNext: Boolean = true,

    isCopy: Boolean = false,
    onCopyChanged: (isCopy: Boolean) -> Unit = {},

    picture: Uri = Uri.EMPTY,
    date: LocalDate = LocalDate.now(),

    professionalId: String = "",
    professionalName: String = "",

    onProfessionalIdChanged: (value: String) -> Unit = {},
    onProfessionalNameChanged: (value: String) -> Unit = {},

    choosePictureSource: () -> Unit = {},
    onSetDate: (date: LocalDate) -> Unit = {},
    onNext: () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Column(
            modifier = Modifier.weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            PrescriptionPicture(
                modifier = Modifier
                    .size(256.dp)
                    .width(256.dp)
                    .height(256.dp),
                picture = picture,
                takePicture = choosePictureSource,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = isCopy,
                    onCheckedChange = onCopyChanged,
                )

                Spacer(modifier = Modifier.width(8.dp))

                // TODO: use string resource
                Text(text = "Usar cópia do lensômetro")
            }
        }

        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        DatePicker(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .weight(1f),
            enabled = !isCopy,
            currDate = date,
            onSetDate = onSetDate,
        )

        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        ProfessionalData(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .weight(2f),

            enabled = !isCopy,

            professionalName = professionalName,
            onProfessionalNameChanged = onProfessionalNameChanged,

            professionalId = professionalId,
            onProfessionalIdChanged = onProfessionalIdChanged,
        )

        PeyessStepperFooter(canGoNext = canGoNext, onNext = onNext)
    }
}

@Composable
fun PrescriptionPicture(
    modifier: Modifier = Modifier,
    picture: Uri = Uri.EMPTY,
    takePicture: () -> Unit = {},
) {
    Box(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                // Clip image to be shaped as a circle
                .matchParentSize()
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
                .clip(CircleShape)
                .clickable { takePicture() },
            model = ImageRequest.Builder(LocalContext.current)
                .data(picture)
                .crossfade(true)
                .size(width = 256, height = 256)
                .build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
            error = painterResource(id = R.drawable.ic_default_placeholder),
            fallback = painterResource(id = R.drawable.ic_default_placeholder),
            placeholder = painterResource(id = R.drawable.ic_default_placeholder),
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
fun DatePicker(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    currDate: LocalDate = LocalDate.now(),
    onSetDate: (date: LocalDate) -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(id = R.string.txt_prescription_date),
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold))

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
                text = currDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
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

    MaterialDialog(dialogState = dialogState,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        buttons = {
            positiveButton(stringResource(id = R.string.dialog_select_date_ok))
            negativeButton(stringResource(id = R.string.dialog_select_date_cancel))
        }) {

        datepicker(
            title = stringResource(id = R.string.dialog_select_date_title),
            initialDate = currDate,
        ) { date ->
            onSetDate(date)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ProfessionalData(
    modifier: Modifier = Modifier,

    enabled: Boolean = false,

    professionalId: String = "",
    professionalName: String = "",

    onProfessionalIdChanged: (value: String) -> Unit = {},
    onProfessionalNameChanged: (value: String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PeyessOutlinedTextField(value = professionalId,
            onValueChange = onProfessionalIdChanged,
            enabled = enabled,
            isError = false,
            errorMessage = "",
            label = { Text(text = stringResource(id = R.string.label_professional_id)) },
            placeholder = { Text(text = stringResource(id = R.string.label_professional_id)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                autoCorrect = false,
                keyboardType = KeyboardType.Number,
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Down) }))

        Spacer(modifier = Modifier.size(SalesAppTheme.dimensions.plane_6))

        PeyessOutlinedTextField(value = professionalName,
            onValueChange = onProfessionalNameChanged,
            enabled = enabled,
            isError = false,
            errorMessage = "",
            label = { Text(text = stringResource(id = R.string.label_professional_name)) },
            placeholder = { Text(text = stringResource(id = R.string.label_professional_name)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }))
    }
}

@Preview
@Composable
private fun PreviewDatePicker() {
    SalesAppTheme {
        DatePicker()
    }
}

@Preview
@Composable
private fun PreviewProfessional() {
    SalesAppTheme {
        ProfessionalData(modifier = Modifier.fillMaxSize())
    }
}

@Preview
@Composable
private fun PreviewPicture() {
    SalesAppTheme {
        PrescriptionPicture(modifier = Modifier.size(256.dp))
    }
}