package com.peyess.salesapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val buttonHeight = 64.dp

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onStartNewSale: () -> Unit = {},
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = SalesAppTheme.dimensions.grid_1_5)
                    .fillMaxWidth()
                    .height(SalesAppTheme.dimensions.minimum_touch_target),
                shape = MaterialTheme.shapes.large,
                onClick = onStartNewSale,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = SalesAppTheme.dimensions.grid_3)
                            .background(color = MaterialTheme.colors.primary.copy(alpha = 0.5f)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "",
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.btn_make_new_sale),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    Home()
}