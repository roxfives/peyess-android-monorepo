package com.peyess.salesapp.feature.sale.prescription_picture

import android.net.Uri
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.prescription_picture.state.PrescriptionPictureState
import com.peyess.salesapp.feature.sale.prescription_picture.state.PrescriptionPictureViewModel
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.file.createPrescriptionFile
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PrescriptionPictureScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val context = LocalContext.current

    val viewModel: PrescriptionPictureViewModel = mavericksViewModel()

    val canGoNext by viewModel.collectAsState(PrescriptionPictureState::canGoNext)
    val isLoading by viewModel.collectAsState(PrescriptionPictureState::isLoading)

    val picture by viewModel.collectAsState(PrescriptionPictureState::pictureUri)
    val date by viewModel.collectAsState(PrescriptionPictureState::prescriptionDate)

    val professionalId by viewModel.collectAsState(PrescriptionPictureState::professionalId)
    val professionalName by viewModel.collectAsState(PrescriptionPictureState::professionalName)

    val pictureFile = remember { createPrescriptionFile(context) }
    val pictureFileUri = remember {
        FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            pictureFile,
        )
    }

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    viewModel.onPictureTaken(pictureFileUri)
                }
            })

    LaunchedEffect(cameraPermissionState) {
        if (cameraPermissionState.permissionRequested && cameraPermissionState.hasPermission) {
            cameraLauncher.launch(pictureFileUri)
        }
    }

    if (!isLoading) {
        PrescriptionPictureImpl(modifier = modifier,

            canGoNext = canGoNext,

            picture = picture,
            date = date,

            professionalId = professionalId,
            professionalName = professionalName,

            onProfessionalIdChanged = viewModel::onProfessionalIdChanged,
            onProfessionalNameChanged = viewModel::onProfessionalNameChanged,

            takePicture = {
                if (cameraPermissionState.hasPermission) {

                    Timber.i("File path is ${pictureFile.absolutePath}")
                    Timber.i("File uri is ${pictureFileUri}")
                    cameraLauncher.launch(pictureFileUri)
                } else {
                    cameraPermissionState.launchPermissionRequest()
                }
            },

            onSetDate = viewModel::onDatePicked,
            onNext = {
                if (canGoNext) {
                    onNext()
                }
            },
        )
    } else {
        PeyessProgressIndicatorInfinite()
    }
}

@Composable
private fun PrescriptionPictureImpl(
    modifier: Modifier = Modifier,

    canGoNext: Boolean = true,

    picture: Uri = Uri.EMPTY,
    date: LocalDate = LocalDate.now(),

    professionalId: String = "",
    professionalName: String = "",

    onProfessionalIdChanged: (value: String) -> Unit = {},
    onProfessionalNameChanged: (value: String) -> Unit = {},

    takePicture: () -> Unit = {},
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
                takePicture = takePicture,
            )
        }

        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        DatePicker(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .weight(1f),
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

            professionalName = professionalName,
            onProfessionalNameChanged = onProfessionalNameChanged,

            professionalId = professionalId,
            onProfessionalIdChanged = onProfessionalIdChanged,
        )

        PeyessNextStep(canGoNext = canGoNext, onNext = onNext)
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
            model = ImageRequest.Builder(LocalContext.current).data(picture).crossfade(true)
                .size(width = 256, height = 256).build(),
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
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                )
                .padding(2.dp)
                .clickable { dialogState.show() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.size(16.dp))

            Text(text = currDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))

            Spacer(modifier = Modifier.size(16.dp))

            IconButton(onClick = { dialogState.show() }) {
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