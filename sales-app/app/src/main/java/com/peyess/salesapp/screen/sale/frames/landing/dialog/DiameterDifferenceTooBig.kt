package com.peyess.salesapp.screen.sale.frames.landing.dialog

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
fun DiameterDifferenceTooBig(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    onConfirm: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(
                text = stringResource(id = R.string.dialog_diameter_too_diff_btn),
                onClick = onConfirm,
            )
        },
    ) {
        title(text = stringResource(id = R.string.dialog_diameter_too_diff_title))
        message(text = stringResource(id = R.string.dialog_diameter_too_diff_message))
    }
}
