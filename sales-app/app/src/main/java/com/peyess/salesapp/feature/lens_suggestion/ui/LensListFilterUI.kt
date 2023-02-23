package com.peyess.salesapp.feature.lens_suggestion.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilter
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

private val errorTitleSpacerHeight = 4.dp

@Composable
internal fun FilterLensDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    @StringRes filterTitleRes: Int = R.string.empty_string,
    filterList: List<LensFilter> = emptyList(),

    isEnabled: Boolean = false,
    isLoading: Boolean = false,
    hasFailed: Boolean = false,
    @StringRes errorMessageRes: Int = R.string.error_generic,
    onRetry: () -> Unit = {},

    onPick: (id: String, name: String) -> Unit = { _, _ -> },
) {
    if (hasFailed) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = errorMessageRes))

            Spacer(modifier = Modifier.height(errorTitleSpacerHeight))

            TextButton(onClick = onRetry) {
                Text(text = stringResource(id = R.string.error_generic_retry))
            }
        }
    } else if (isLoading) {
        CircularProgressIndicator()
    } else {
        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.lens_filter_action_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_type_none))
            val options = noneOption + filterList.map { it.name }

            title(res = filterTitleRes)

            listItems(list = options) { index, _ ->

                if (index > 0) {
                    onPick(filterList[index - 1].id, filterList[index - 1].name)
                } else {
                    onPick("", "")
                }

                dialogState.hide()
            }
        }
    }
}