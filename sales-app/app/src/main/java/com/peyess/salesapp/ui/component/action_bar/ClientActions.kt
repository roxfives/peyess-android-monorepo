package com.peyess.salesapp.ui.component.action_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val clientSearchSpacer = 16.dp
private val clientSearchRoundedCornerShape = 32.dp

@Composable
fun ClientActions(
    modifier: Modifier = Modifier,

    onCreateNewClient: () -> Unit = {},
    onSearchClient: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .weight(1f),
            shape = RoundedCornerShape(clientSearchRoundedCornerShape),
            onClick = onSearchClient,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "")

                Spacer(modifier = Modifier.width(clientSearchSpacer))

                Text(text = stringResource(id = R.string.btn_client_action_search_client))
            }
        }

        Spacer(modifier = Modifier.width(clientSearchSpacer))

        IconButton(
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = MaterialTheme.colors.primary,
                )
//                .border(
//                    border = BorderStroke(
//                        width = clientAddButtonBorderWidth,
//                        color = MaterialTheme.colors.primary.copy(alpha = 0.2f),
//                    ),
//                    shape = CircleShape,
//                )
                .size(SalesAppTheme.dimensions.minimum_touch_target),
            onClick = onCreateNewClient,
        ) {
            Icon(
                imageVector = Icons.Filled.PersonAdd,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
fun ClientActionsPreview(
    modifier: Modifier = Modifier,
) {
    SalesAppTheme {
        ClientActions()
    }
}