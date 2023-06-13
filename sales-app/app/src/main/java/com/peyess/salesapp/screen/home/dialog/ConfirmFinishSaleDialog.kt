package com.peyess.salesapp.screen.home.dialog

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.DialogProperties
import com.peyess.salesapp.R
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConfirmFinishSaleDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    onConfirmFinish: () -> Unit = {},
    onCancelFinish: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(stringResource(id = R.string.dialog_finish_sale_confirm)) {
                onConfirmFinish()
            }

            negativeButton(text = stringResource(id = R.string.dialog_finish_sale_cancel)) {
                onCancelFinish()
            }
        }
    ) {
        iconTitle(text = stringResource(id = R.string.dialog_finish_sale_title)) {
            Icon(imageVector = Icons.Filled.Sell, contentDescription = "")
        }

        message(stringResource(id = R.string.dialog_finish_sale_content))
    }
}