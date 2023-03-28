package com.peyess.salesapp.ui.component.date

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.peyess.salesapp.R
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PeyessDialogDatePicker(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    title: String = "",
    currDate: LocalDate = LocalDate.now(),
    onSetDate: (date: LocalDate) -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
        )

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

    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(stringResource(id = R.string.dialog_select_date_ok))
            negativeButton(stringResource(id = R.string.dialog_select_date_cancel))
        },
    ) {

        datepicker(
            title = title,
            initialDate = currDate,
        ) { date ->
            onSetDate(date)
        }
    }
}