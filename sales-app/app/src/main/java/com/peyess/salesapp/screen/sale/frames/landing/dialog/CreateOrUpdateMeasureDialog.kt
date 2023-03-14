package com.peyess.salesapp.screen.sale.frames.landing.dialog

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PanoramaFishEye
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.peyess.salesapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

@Composable
fun CreateOrUpdateMeasureDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    onConfirmUpdate: () -> Unit = {},
    onConfirmOverwrite: () -> Unit = {},
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.dialog_overwrite_btn_update)) {
                onConfirmUpdate()
            }

            negativeButton(
                text = stringResource(id = R.string.dialog_overwrite_btn_overwrite),
                textStyle = TextStyle.Default.copy(color = MaterialTheme.colors.error)
            ) {
                onConfirmOverwrite()
            }
        }
    ) {
        iconTitle(text = stringResource(id = R.string.dialog_overwrite_title)) {
            Icon(imageVector = Icons.Filled.SquareFoot, contentDescription = "")
        }

        message(stringResource(id = R.string.dialog_overwrite_message))
    }
}