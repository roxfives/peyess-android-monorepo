package com.peyess.salesapp.ui.component.chip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier

@Composable
fun <T> PeyessChipGroup(
    modifier: Modifier = Modifier,
    keepSameWidth: Boolean = true,
    items: List<T> = emptyList(),
    itemName: (T?) -> String = { "" },
    selected: T? = null,
    onSelectedChanged: (String) -> Unit = {},
) {
    val chipModifier = if (keepSameWidth) {
        val density = LocalDensity.current
        val minimumWidthState = remember { MinimumWidthState() }

        Modifier.minimumWidthModifier(
            minimumWidthState,
            density
        )
    } else {
        Modifier
    }



    Column(modifier = modifier.padding(8.dp)) {
        LazyRow {
            items(items.size) {
                PeyessChip(
                    modifier = chipModifier.padding(horizontal = 8.dp),
                    name = itemName(items[it]),
                    isSelected = if (selected != null) itemName(selected) == itemName(items[it]) else false,
                    onSelectionChanged = { name ->
                        onSelectedChanged(name)
                    },
                )
            }
        }
    }
}
