package com.peyess.salesapp.ui.component.modifier

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class MinimumHeightState(minHeight: Dp? = null) {
    var minHeight by mutableStateOf(minHeight)
}

fun Modifier.minimumHeightModifier(state: MinimumHeightState, density: Density) = onSizeChanged { size ->
    val itemHeight = with(density) {
        val height = size.height
        height.toDp()
    }

    if (itemHeight > (state.minHeight ?: 0.dp)) {
        state.minHeight = itemHeight
    }
}.defaultMinSize(minHeight = state.minHeight ?: Dp.Unspecified)