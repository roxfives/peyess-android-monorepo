package com.peyess.salesapp.feature.settings_actions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallToAction
import androidx.compose.material.icons.filled.SystemUpdate
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
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.settings_actions.state.SettingsAndActionState
import com.peyess.salesapp.feature.settings_actions.state.SettingsAndActionViewModel
import com.peyess.salesapp.ui.component.footer.StepperFooter
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
    )
}

@Composable
private fun SettingsAndActionsScreenImpl(
    modifier: Modifier = Modifier,
    isUpdatingProductsTable: Boolean = false,
    onUpdateProductsTable: () -> Unit = {},
) {
    Column(
        modifier = modifier,
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
            onRunAction = onUpdateProductsTable,
        )
    }
}

@Composable
private fun ActionTile(
    modifier: Modifier = Modifier,
    actionIcon: @Composable  () -> Unit = {},
    actionTitle: @Composable  () -> Unit = {},
    enabled: Boolean = false,
    onRunAction: () -> Unit = {},
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
