package com.peyess.salesapp.feature.sale.service_order.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.service_order.isCreatingParam
import com.peyess.salesapp.navigation.sale.service_order.serviceOrderIdParam
import com.peyess.salesapp.navigation.sale.service_order.saleIdParam

@Composable
private fun parseParameterIsCreating(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: Boolean = false,
    onUpdate: (value: Boolean) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val isEditing = args
        ?.getBoolean(isCreatingParam, defaultValue)
        ?: defaultValue

    onUpdate(isEditing)
}

@Composable
private fun parseParameterServiceOrderId(
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
private fun parseParameterSaleId(
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
fun parseParameters(
    navController: NavHostController,
    onUpdateIsCreating: (Boolean) -> Unit,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit
) {
    parseParameterIsCreating(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateIsCreating,
    )

    parseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId,
    )

    parseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId,
    )
}
