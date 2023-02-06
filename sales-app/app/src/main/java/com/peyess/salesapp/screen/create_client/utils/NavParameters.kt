package com.peyess.salesapp.screen.create_client.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.clientIdParam
import com.peyess.salesapp.navigation.create_client.createScenarioParam
import com.peyess.salesapp.navigation.create_client.paymentIdParam

@Composable
private fun parseParameterClientId(
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
private fun parseParameterPaymentId(
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
private fun parseParameterCreateScenario(
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
fun parseParameters(
    navController: NavHostController,
    onUpdateClientId: (String) -> Unit,
    onUpdatePaymentId: (Long) -> Unit,
    onUpdateCreateScenario: (CreateScenario) -> Unit,
) {

    parseParameterClientId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateClientId
    )

    parseParameterPaymentId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePaymentId
    )

    parseParameterCreateScenario(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateCreateScenario,
    )
}
