package com.peyess.salesapp.screen.sale.payment.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.payment.paymentIdParam
import com.peyess.salesapp.navigation.sale.payment.saleIdParam
import com.peyess.salesapp.navigation.sale.payment.serviceOrderIdParam

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
private fun parseParameterClientId(
    backStackEntry: NavBackStackEntry? = null,
    defaultValue: String = "",
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val clientId = args
        ?.getString("clientId", defaultValue)
        ?: defaultValue

    onUpdate(clientId)
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
    onUpdatePaymentId: (Long) -> Unit,
    onUpdateClientId: (String) -> Unit,
    onUpdateSaleId: (String) -> Unit,
    onUpdateServiceOrderId: (String) -> Unit
) {
    parseParameterPaymentId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdatePaymentId
    )

    parseParameterClientId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateClientId
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
