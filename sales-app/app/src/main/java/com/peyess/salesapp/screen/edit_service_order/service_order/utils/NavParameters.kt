package com.peyess.salesapp.screen.edit_service_order.service_order.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.edit_service_order.service_order.reloadFromServerParam
import com.peyess.salesapp.navigation.sale.service_order.serviceOrderIdParam
import com.peyess.salesapp.navigation.sale.service_order.saleIdParam

@Composable
private fun ParseParameterServiceOrderId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val serviceOrderId = args
        ?.getString(serviceOrderIdParam, defaultValue)
        ?: defaultValue

    onUpdate(serviceOrderId)
}

@Composable
private fun ParseParameterSaleId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val saleId = args
        ?.getString(saleIdParam, defaultValue)
        ?: defaultValue

    onUpdate(saleId)
}

@Composable
private fun ParseParameterReloadFromServer(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: Boolean = false,
    onUpdate: (value: Boolean) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val saleId = args
        ?.getBoolean(reloadFromServerParam, defaultValue)
        ?: defaultValue

    onUpdate(saleId)
}

@Composable
fun ParseParameters(
    navController: NavHostController,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit,
    onUpdateReloadFromServer: (Boolean) -> Unit,
) {
    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId,
    )

    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId,
    )

    ParseParameterReloadFromServer(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateReloadFromServer,
    )
}
