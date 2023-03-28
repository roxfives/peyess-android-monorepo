package com.peyess.salesapp.screen.edit_service_order.payment_fee.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.edit_service_order.payment_fee.fullPriceParam
import com.peyess.salesapp.navigation.edit_service_order.payment_fee.saleIdParam
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
private fun ParseParameterSaleId(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val saleId = args?.getString(saleIdParam, "")

    if (!saleId.isNullOrBlank()) {
        onUpdate(saleId)
    }
}

@Composable
private fun ParseParameterFullPrice(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: BigDecimal) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val fullPriceStr = args?.getString(fullPriceParam, "0")

    if (!fullPriceStr.isNullOrBlank()) {
        onUpdate(BigDecimal(fullPriceStr).setScale(2, RoundingMode.HALF_EVEN))
    }
}

@Composable
fun ParseParameters(
    navController: NavHostController,
    onUpdateSaleId: (String) -> Unit,
    onUpdateFullPrice: (BigDecimal) -> Unit
) {
    ParseParameterSaleId(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateSaleId,
    )

    ParseParameterFullPrice(
        backStackEntry = navController.currentBackStackEntry,
        onUpdate = onUpdateFullPrice,
    )
}