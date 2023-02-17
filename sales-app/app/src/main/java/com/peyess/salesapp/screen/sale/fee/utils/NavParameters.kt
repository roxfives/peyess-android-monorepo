package com.peyess.salesapp.screen.sale.fee.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.peyess.salesapp.navigation.sale.fee.fullPriceArgumentName
import com.peyess.salesapp.navigation.sale.fee.saleIdArgumentName
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
private fun ParseParameterSaleId(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val saleId = args?.getString(saleIdArgumentName, "")

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
    val fullPriceStr = args?.getString(fullPriceArgumentName, "0")

    if (!fullPriceStr.isNullOrBlank()) {
        onUpdate(BigDecimal(fullPriceStr).setScale(2, RoundingMode.HALF_EVEN))
    }
}
