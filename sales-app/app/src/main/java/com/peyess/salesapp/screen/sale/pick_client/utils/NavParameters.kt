package com.peyess.salesapp.screen.sale.pick_client.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.navigation.client_list.paymentIdParam
import com.peyess.salesapp.navigation.client_list.pickScenarioParam
import com.peyess.salesapp.navigation.client_list.saleIdParam
import com.peyess.salesapp.navigation.client_list.serviceOrderIdParam

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
fun ParseParameters(
    navController: NavHostController,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit,
    onUpdatePaymentId: (Long) -> Unit,
    onUpdateCreateScenario: (PickScenario) -> Unit,
) {
    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId
    )

    ParseParameterServiceOrderId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateServiceOrderId
    )

    ParseParameterPaymentId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePaymentId
    )

    ParseParameterCreateScenario(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateCreateScenario,
    )
}
