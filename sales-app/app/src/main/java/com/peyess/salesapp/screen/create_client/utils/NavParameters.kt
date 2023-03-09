package com.peyess.salesapp.screen.create_client.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.clientIdParam
import com.peyess.salesapp.navigation.create_client.createScenarioParam
import com.peyess.salesapp.navigation.create_client.paymentIdParam
import com.peyess.salesapp.navigation.create_client.saleIdParam
import com.peyess.salesapp.navigation.create_client.serviceOrderIdParam
import com.peyess.salesapp.navigation.create_client.updateExistingClientParam

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
private fun ParseParameterClientId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val clientId = args
        ?.getString(clientIdParam, defaultValue)
        ?: defaultValue

    onUpdate(clientId)
}

@Composable
private fun ParseParameterPaymentId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: Long = 0L,
    onUpdate: (value: Long) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val paymentId = args
        ?.getLong(paymentIdParam, defaultValue)
        ?: defaultValue

    onUpdate(paymentId)
}

@Composable
private fun ParseParameterCreateScenario(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: CreateScenario) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val createScenarioArg = args
        ?.getString(createScenarioParam, defaultValue)
        ?: defaultValue

    val createScenario = CreateScenario.fromName(createScenarioArg) ?: CreateScenario.Home

    onUpdate(createScenario)
}

@Composable
private fun ParseParameterIsUpdatingExistingClient(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: Boolean = false,
    onUpdate: (value: Boolean) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val isUpdating = args
        ?.getBoolean(updateExistingClientParam, defaultValue)
        ?: defaultValue

    onUpdate(isUpdating)
}

@Composable
fun ParseParameters(
    navController: NavHostController,
    onUpdateClientId: (String) -> Unit,
    onUpdatePaymentId: (Long) -> Unit,
    onUpdateCreateScenario: (CreateScenario) -> Unit,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit,
    onUpdateExistingClient: (Boolean) -> Unit,
) {
    ParseParameterClientId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateClientId,
    )

    ParseParameterPaymentId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePaymentId,
    )

    ParseParameterCreateScenario(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateCreateScenario,
    )

    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId,
    )

    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId,
    )

    ParseParameterIsUpdatingExistingClient(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateExistingClient,
    )
}
