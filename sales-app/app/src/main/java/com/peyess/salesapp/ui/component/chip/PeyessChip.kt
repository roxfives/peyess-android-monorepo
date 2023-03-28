package com.peyess.salesapp.ui.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun PeyessChip(
    modifier: Modifier = Modifier,
    name: String = "",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
) {
    Surface(
        modifier = modifier.padding(4.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.large.copy(CornerSize(50)),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
        color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background,
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onSelectionChanged(name)
                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.body2,
                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ChipPreview() {
    SalesAppTheme {
        PeyessChip(modifier = Modifier.fillMaxWidth(), name = "Preview")
    }
}