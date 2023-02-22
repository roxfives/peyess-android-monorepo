package com.peyess.salesapp.screen.sale.prescription_picture

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.peyess.salesapp.feature.prescription.prescription_picture.PrescriptionPictureUI
import com.peyess.salesapp.screen.sale.prescription_picture.state.PrescriptionPictureState
import com.peyess.salesapp.screen.sale.prescription_picture.state.PrescriptionPictureViewModel
import com.peyess.salesapp.navigation.sale.prescription.isUpdatingParam
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.utils.file.createPrescriptionFile

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

    val isCopy by viewModel.collectAsState(PrescriptionPictureState::isCopy)

    val pictureFile = remember { createPrescriptionFile(context) }
    val pictureFileUri = remember {
        FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            pictureFile,
        )
    }

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
        PrescriptionPictureUI(
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
