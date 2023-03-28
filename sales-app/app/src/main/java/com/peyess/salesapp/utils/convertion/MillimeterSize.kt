package com.peyess.salesapp.utils.convertion

import androidx.compose.ui.unit.Dp

fun dpFrom(size: Float): Dp {
    return Dp(size * (16f / 2.54f))
}