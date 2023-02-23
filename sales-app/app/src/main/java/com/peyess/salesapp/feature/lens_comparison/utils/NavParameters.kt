package com.peyess.salesapp.feature.lens_comparison.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.lenses.isEditingParam
import com.peyess.salesapp.navigation.sale.lenses.saleIdArgumentName
import com.peyess.salesapp.navigation.sale.lenses.soIdArgumentName

@Composable
private fun ParseParameterIsEditing(
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
fun ParseParameters(
    navController: NavHostController,
    onUpdateIsEditing: (Boolean) -> Unit,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit
) {
    ParseParameterIsEditing(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateIsEditing,
    )

    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId,
    )

    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId,
    )
}
