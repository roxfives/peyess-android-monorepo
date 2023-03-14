package com.peyess.salesapp.screen.sale.frames.landing.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import com.peyess.salesapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun DiameterDifferenceTooBig(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    onDismiss: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.dialog_diameter_too_diff_btn),
                onClick = onDismiss,
            )
        },
    ) {
        title(text = stringResource(id = R.string.dialog_diameter_too_diff_title))
        message(text = stringResource(id = R.string.dialog_diameter_too_diff_message))
    }
}
