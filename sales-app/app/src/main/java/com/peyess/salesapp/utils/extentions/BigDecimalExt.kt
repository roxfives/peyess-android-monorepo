package com.peyess.salesapp.utils.extentions

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.roundToDouble(roundValue: Boolean, scale: Int = 2): Double {
    return if (roundValue) {
        setScale(scale, RoundingMode.HALF_UP).toDouble()
    } else {
        toDouble()
    }
}