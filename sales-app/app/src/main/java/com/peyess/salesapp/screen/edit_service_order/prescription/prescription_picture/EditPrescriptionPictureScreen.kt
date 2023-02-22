package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture

import android.Manifest
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
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.state.EditPrescriptionPictureState
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.state.EditPrescriptionPictureViewModel
import com.peyess.salesapp.screen.edit_service_order.prescription.utils.ParseParameters
import com.peyess.salesapp.utils.file.createPrescriptionFile

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditPrescriptionPictureScreen(
    modifier: Modifier,
    navHostController: NavHostController = rememberNavController(),

    onNext: (saleId: String, serviceOrderId: String, prescriptionId: String) -> Unit = { _, _, _ -> },
) {
    val context = LocalContext.current

    val viewModel: EditPrescriptionPictureViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onSetSaleId,
        onUpdateServiceOrderId = viewModel::onSetServiceOrderId,
        onUpdatePrescriptionId = viewModel::onSetPrescriptionId,
    )

    val saleId by viewModel.collectAsState(EditPrescriptionPictureState::saleId)
    val serviceOrderId by viewModel.collectAsState(EditPrescriptionPictureState::serviceOrderId)
    val prescriptionId by viewModel.collectAsState(EditPrescriptionPictureState::prescriptionId)

    val picture by viewModel.collectAsState(EditPrescriptionPictureState::pictureUri)
    val isCopy by viewModel.collectAsState(EditPrescriptionPictureState::isCopy)
    val date by viewModel.collectAsState(EditPrescriptionPictureState::prescriptionDate)
    val professionalId by viewModel.collectAsState(EditPrescriptionPictureState::professionalIdInput)
    val professionalName by viewModel.collectAsState(EditPrescriptionPictureState::professionalNameInput)

    val canGoNext by viewModel.collectAsState(EditPrescriptionPictureState::canGoNext)

    val pictureFile = remember { createPrescriptionFile(context) }
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
                viewModel.onPictureTaken(pictureFileUri)
            }
        }
    )

    val filesPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        rememberPermissionState(
            Manifest.permission.READ_EXTERNAL_STORAGE
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

    PrescriptionPictureUI(
        modifier = modifier,

        isCopy = isCopy,
        picture = picture,
        date = date.toLocalDate(),
        professionalId = professionalId,
        professionalName = professionalName,
        onCopyChanged = viewModel::onCopyChanged,
        onSetDate = viewModel::onDatePicked,
        onProfessionalIdChanged = viewModel::onProfessionalIdChanged,
        onProfessionalNameChanged = viewModel::onProfessionalNameChanged,

        canGoNext = canGoNext,

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

        onNext = { onNext(saleId, serviceOrderId, prescriptionId) },
    )
}
