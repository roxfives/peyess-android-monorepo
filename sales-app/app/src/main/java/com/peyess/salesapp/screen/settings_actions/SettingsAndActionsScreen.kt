package com.peyess.salesapp.screen.settings_actions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallToAction
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.settings_actions.state.SettingsAndActionState
import com.peyess.salesapp.screen.settings_actions.state.SettingsAndActionViewModel
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val listButtonPadding = 8.dp
private val listButtonIconSpacer = 16.dp
private val listButtonHeight = 120.dp

@Composable
fun SettingsAndActionsScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel: SettingsAndActionViewModel = mavericksViewModel()

    val isUpdatingProductsTable by viewModel
        .collectAsState(SettingsAndActionState::isUpdatingProductsTable)

    SettingsAndActionsScreenImpl(
        modifier = modifier,

        isUpdatingProductsTable = isUpdatingProductsTable,
        onUpdateProductsTable = viewModel::updateProductsTable,
        onCancelProductsUpdate = viewModel::cancelProductsUpdate,
    )
}

@Composable
private fun SettingsAndActionsScreenImpl(
    modifier: Modifier = Modifier,
    isUpdatingProductsTable: Boolean = false,
    onUpdateProductsTable: () -> Unit = {},
    onCancelProductsUpdate: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ActionTile(
            actionIcon = { 
                if (isUpdatingProductsTable) {
                    CircularProgressIndicator()
                } else {
                    Icon(imageVector = Icons.Filled.Update, contentDescription = "")
                }
            },
            actionTitle = {
                if (isUpdatingProductsTable) {
                    Text(text = stringResource(id = R.string.settings_title_updating_products))
                } else {
                    Text(text = stringResource(id = R.string.settings_title_update_products))
                }
            },
            enabled = !isUpdatingProductsTable,
            isActionRunning = isUpdatingProductsTable,
            onRunAction = onUpdateProductsTable,
            onCancelAction = onCancelProductsUpdate,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier.padding(listButtonPadding),
            text = "Build version: ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
private fun ActionTile(
    modifier: Modifier = Modifier,
    actionIcon: @Composable  () -> Unit = {},
    actionTitle: @Composable  () -> Unit = {},
    enabled: Boolean = false,
    isActionRunning: Boolean = false,
    onRunAction: () -> Unit = {},
    onCancelAction: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(listButtonPadding)
            .height(listButtonHeight)
            .fillMaxWidth()
            .clickable { onRunAction() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(listButtonPadding))

        actionIcon()

        Spacer(modifier = Modifier.width(listButtonIconSpacer))
        actionTitle()

        Spacer(modifier = Modifier.weight(1f))

        AnimatedVisibility(
            visible = isActionRunning,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            IconButton(
                modifier = Modifier.size(SalesAppTheme.dimensions.minimum_touch_target)
                    .background(
                        color = MaterialTheme.colors.error,
                        shape = CircleShape,
                    ),
                enabled = isActionRunning,
                onClick = onCancelAction,
            ) {
                Icon(
                    imageVector = Icons.Filled.Cancel,
                    tint = MaterialTheme.colors.onError,
                    contentDescription = "",
                )
            }
        }

        Spacer(modifier = Modifier.width(listButtonIconSpacer))
    }
}

@Preview
@Composable
private fun SettingsAndActionsScreenImplUpdatingPreview() {
    SalesAppTheme {
        SettingsAndActionsScreenImpl(
            isUpdatingProductsTable = true,
        )
    }
}

@Preview
@Composable
private fun SettingsAndActionsScreenImplStalePreview() {
    SalesAppTheme {
        SettingsAndActionsScreenImpl(
            isUpdatingProductsTable = false,
        )
    }
}

@Preview
@Composable
private fun ActionButtonPreview() {
    SalesAppTheme {
        ActionTile(
            actionIcon = {
                Icon(imageVector = Icons.Filled.CallToAction, contentDescription = "")
            },
            actionTitle = {
                Text(text = "Fazer ação...")
            },
        )
    }
}
