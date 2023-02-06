package com.peyess.salesapp.screen.home.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.peyess.salesapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun ExistingClientDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    onCreateNewClient: () -> Unit = {},
    onUseExistingClient: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
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
