package com.peyess.salesapp.screen.edit_service_order.prescription.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.edit_service_order.prescription.prescriptionIdParam
import com.peyess.salesapp.navigation.edit_service_order.prescription.serviceOrderIdParam

@Composable
private fun ParseParameterServiceOrderId(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val serviceOrderId = args?.getString(serviceOrderIdParam, "")

    if (!serviceOrderId.isNullOrBlank()) {
        onUpdate(serviceOrderId)
    }
}

@Composable
private fun ParseParameterPrescriptionId(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val prescriptionId = args?.getString(prescriptionIdParam, "")

    if (!prescriptionId.isNullOrBlank()) {
        onUpdate(prescriptionId)
    }
}

@Composable
fun ParseParameters(
    navController: NavHostController,
    onUpdateServiceOrderId: (String) -> Unit,
    onUpdatePrescriptionId: (String) -> Unit,
) {
    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId,
    )

    ParseParameterPrescriptionId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePrescriptionId,
    )
}
