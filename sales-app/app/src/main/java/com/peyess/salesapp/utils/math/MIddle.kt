package com.peyess.salesapp.utils.math

import kotlin.math.absoluteValue
import kotlin.math.max

fun middle(a: Double, b: Double): Double {
    return max(a, b) - ((a - b).absoluteValue / 2.0)
}