package com.peyess.salesapp.screen.sale.lens_suggestion.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.lens_pick.isEditingParam
import com.peyess.salesapp.navigation.sale.lens_pick.saleIdArgumentName
import com.peyess.salesapp.navigation.sale.lens_pick.soIdArgumentName

@Composable
private fun parseParameterIsEditing(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: Boolean = false,
    onUpdate: (value: Boolean) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val isEditing = args
        ?.getBoolean(isEditingParam, defaultValue)
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
        ?.getString(soIdArgumentName, defaultValue)
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
        ?.getString(saleIdArgumentName, defaultValue)
        ?: defaultValue

    onUpdate(saleId)
}

@Composable
fun parseParameters(
    navController: NavHostController,
    onUpdateIsEditing: (Boolean) -> Unit,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit
) {
    parseParameterIsEditing(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateIsEditing
    )

    parseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId
    )

    parseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId
    )
}
