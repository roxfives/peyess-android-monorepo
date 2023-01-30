package com.peyess.salesapp.ui.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun PeyessContentChip(
    modifier: Modifier = Modifier,

    content: @Composable () -> Unit = {},
    toggleOnBackgroundColor: Color = MaterialTheme.colors.background,
    toggleOnBorderColor: Color = MaterialTheme.colors.primary,
    toggleOnTextColor: Color = MaterialTheme.colors.onBackground,
    toggleOffBackgroundColor: Color = MaterialTheme.colors.background,
    toggleOffBorderColor: Color = MaterialTheme.colors.primary,
    toggleOffTextColor: Color = MaterialTheme.colors.onBackground,

    isSelected: Boolean = false,
    onSelectionChanged: (isSelected: Boolean) -> Unit = {},
) {
    Button(
        modifier = modifier.padding(0.dp),
        shape = MaterialTheme.shapes.large.copy(CornerSize(50)),
        border = BorderStroke(
            width = 2.dp,
            color = if (isSelected) toggleOnBorderColor else toggleOffBorderColor,
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) toggleOnBackgroundColor else toggleOffBackgroundColor,
        ),
        onClick = { onSelectionChanged(!isSelected) },
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProvideTextStyle(
                MaterialTheme.typography.body1.copy(
                    color = if (isSelected) toggleOnTextColor else toggleOffTextColor
                )
            ) {
                content()
            }
        }
    }

//    Button(
//        modifier = modifier.padding(4.dp),
//        shape = MaterialTheme.shapes.large.copy(CornerSize(50)),
//        border = BorderStroke(
//            width = 2.dp,
//            color = if (isSelected) toggleOnBorderColor else toggleOffBorderColor,
//        ),
//        onClick = { onSelectionChanged(!isSelected) },
//    ) {
//        Row(
//            modifier = modifier
//                .padding(14.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            ProvideTextStyle(
//                MaterialTheme.typography.body1.copy(
//                    color = if (isSelected) toggleOnTextColor else toggleOffTextColor
//                )
//            ) {
//                content()
//            }
//        }
//    }
}

@Preview
@Composable
private fun ChipPreview() {
    SalesAppTheme {
        PeyessContentChip(
            content = {
                Row {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = ""
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(text = "Content preview")
                }
            }
        )
    }
}