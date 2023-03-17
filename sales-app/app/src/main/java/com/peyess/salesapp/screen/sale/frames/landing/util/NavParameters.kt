package com.peyess.salesapp.screen.sale.frames.landing.util

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.frames.isEditingParam

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
fun ParseParameters(
    navController: NavHostController,
    onUpdateIsEditing: (Boolean) -> Unit,
) {
    ParseParameterIsEditing(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateIsEditing,
    )
}
