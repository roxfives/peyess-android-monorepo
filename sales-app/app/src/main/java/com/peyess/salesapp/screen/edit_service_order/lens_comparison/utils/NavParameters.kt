package com.peyess.salesapp.screen.edit_service_order.lens_comparison.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.edit_service_order.lenses.saleIdArgumentName
import com.peyess.salesapp.navigation.edit_service_order.lenses.soIdArgumentName

@Composable
private fun ParseParameterSaleId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val saleId = args
        ?.getString(saleIdArgumentName, defaultValue)
        ?: defaultValue

    onUpdate(saleId)
}

@Composable
private fun ParseParameterServiceOrderId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val serviceOrderId = args
        ?.getString(soIdArgumentName, defaultValue)
        ?: defaultValue

    onUpdate(serviceOrderId)
}

@Composable
fun ParseParameters(
    navController: NavHostController,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit
) {
    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId
    )

    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId
    )
}
