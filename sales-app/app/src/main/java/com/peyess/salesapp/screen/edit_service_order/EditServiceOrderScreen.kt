package com.peyess.salesapp.screen.edit_service_order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peyess.salesapp.feature.service_order.ServiceOrderUI

@Composable
fun EditServiceOrderScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    ServiceOrderUI(
        modifier = modifier,
    )
}
