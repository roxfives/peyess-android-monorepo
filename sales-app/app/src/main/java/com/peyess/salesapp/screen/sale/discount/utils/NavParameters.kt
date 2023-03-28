package com.peyess.salesapp.screen.sale.discount.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.sale.discount.fullPriceArgumentName
import com.peyess.salesapp.navigation.sale.discount.saleIdArgumentName
import java.math.BigDecimal

@Composable
fun parseParameterSaleId(
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
fun parseParameterFullPrice(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: BigDecimal) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val fullPriceStr = args?.getString(fullPriceArgumentName, "0")

    if (!fullPriceStr.isNullOrBlank()) {
        onUpdate(BigDecimal(fullPriceStr).setScale(2))
    }
}