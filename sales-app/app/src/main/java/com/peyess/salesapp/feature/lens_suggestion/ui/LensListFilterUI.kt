package com.peyess.salesapp.feature.lens_suggestion.ui

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.lens_suggestion.model.LensFilter
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val errorTitleSpacerHeight = 4.dp

@OptIn(ExperimentalComposeUiApi::class)
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
    val scope = rememberCoroutineScope()

    // Workaround in case the list changes when the dialog is open
    // https://github.com/vanpra/compose-material-dialogs/issues/122
    // TODO: When the issue is fixed, remove this workaround and the if around the dialogs
    LaunchedEffect(filterList) {
        // If the list changes when the dialog is open, we should refresh the dialog manually
        // by closing and opening it again
        if (dialogState.showing) {
            dialogState.hide()
            scope.launch {
                // this won't work without adding delay
                delay(50)
                dialogState.show()
            }
        }
    }

    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            negativeButton(stringResource(id = R.string.lens_filter_action_cancel))
        },
    ) {
        title(res = filterTitleRes)

        val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_type_none))
        val options = noneOption + filterList.map { it.name }

        listItems(list = options) { index, _ ->
            if (index > 0) {
                onPick(filterList[index - 1].id, filterList[index - 1].name)
            } else {
                onPick("", "")
            }

            dialogState.hide()
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AnimatedVisibility(
                visible = hasFailed,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(text = stringResource(id = errorMessageRes))

                    Spacer(modifier = Modifier.height(errorTitleSpacerHeight))

                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(id = R.string.error_generic_retry))
                    }
                }
            }

            AnimatedVisibility(
                visible = !hasFailed && isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
