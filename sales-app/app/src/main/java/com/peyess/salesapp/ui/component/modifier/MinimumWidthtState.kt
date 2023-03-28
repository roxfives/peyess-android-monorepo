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


class MinimumWidthState(minWidth: Dp? = null) {
    var minWidth by mutableStateOf(minWidth)
}

fun Modifier.minimumWidthModifier(state: MinimumWidthState, density: Density) = onSizeChanged { size ->
    val itemWidth = with(density) {
        val width = size.width
        width.toDp()
    }

    if (itemWidth > (state.minWidth ?: 0.dp)) {
        state.minWidth = itemWidth
    }
}.defaultMinSize(minWidth = state.minWidth ?: Dp.Unspecified)