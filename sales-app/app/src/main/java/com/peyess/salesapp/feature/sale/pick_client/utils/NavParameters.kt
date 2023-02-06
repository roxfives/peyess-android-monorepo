package com.peyess.salesapp.feature.sale.pick_client.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.navigation.pick_client.pickScenarioParam

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
    onUpdate: (value: PickScenario) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val pickScenarioArg = args
        ?.getString(pickScenarioParam, defaultValue)
        ?: defaultValue

    val createScenario = PickScenario.fromName(pickScenarioArg) ?: PickScenario.ServiceOrder

    onUpdate(createScenario)
}

@Composable
fun parseParameters(
    navController: NavHostController,
    onUpdatePaymentId: (Long) -> Unit,
    onUpdateCreateScenario: (PickScenario) -> Unit,
) {
    parseParameterPaymentId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePaymentId
    )

    parseParameterCreateScenario(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateCreateScenario,
    )
}