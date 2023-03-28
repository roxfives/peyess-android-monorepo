package com.peyess.salesapp.screen.home.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.peyess.salesapp.R
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ExistingClientDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    onCreateNewClient: () -> Unit = {},
    onUseExistingClient: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(stringResource(R.string.existing_client_dialog_btn_reuse)) {
                onUseExistingClient()
            }

            negativeButton(stringResource(R.string.existing_client_dialog_btn_create_new)) {
                onCreateNewClient()
            }
        },
    ) {
        title(text = stringResource(R.string.existing_client_dialog_title))
        message(text = stringResource(R.string.existing_client_dialog_content))
    }
}
