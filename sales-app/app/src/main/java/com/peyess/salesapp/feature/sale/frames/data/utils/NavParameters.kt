package com.peyess.salesapp.feature.sale.frames.data.utils

import androidx.navigation.NavBackStackEntry
import com.peyess.salesapp.navigation.sale.frames.serviceOrderParamName

private fun parseParameterServiceOrderId(
    backStackEntry: NavBackStackEntry? = null,
    onUpdate: (value: String) -> Unit = {}
) {
    val args = backStackEntry?.arguments
    val serviceOrderId = args?.getString(serviceOrderParamName, "")

    if (!serviceOrderId.isNullOrBlank()) {
        onUpdate(serviceOrderId)
    }
}

fun parseParameters(
    backStackEntry: NavBackStackEntry? = null,
    onUpdateServiceId: (serviceOrderId: String) -> Unit = {}
) {
    parseParameterServiceOrderId(backStackEntry) { onUpdateServiceId(it) }
}