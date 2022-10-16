package com.peyess.salesapp.ui.component.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.ui.theme.PeyessBlue
import com.peyess.salesapp.ui.theme.PrimaryDark
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val subtitleAlpha = 0.5f

private val minButtonWidth = 160.dp
private val maxButtonWidth = 200.dp

private val minButtonHeight = 72.dp
private val maxButtonHeight = 120.dp

private val buttonSpacerWidth = 16.dp

@Composable
fun HomeScreenButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit = {},

    title: String = "",
    subtitle: String = "",

    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = Color.White,
        contentColor = PeyessBlue,
        disabledBackgroundColor = Color.Gray.copy(alpha = 0.6f),
        disabledContentColor = PrimaryDark,
    ),

    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier
            .widthIn(minButtonWidth, maxButtonWidth)
            .heightIn(minButtonHeight, maxButtonHeight),
        enabled = enabled,
        colors = colors,
        onClick = onClick,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.weight(1f),
            ) {
                Box(modifier = Modifier.align(Center)) {
                    icon()
                }
            }

            Spacer(modifier = Modifier.width(buttonSpacerWidth))

            Column(
                modifier = Modifier.weight(3f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6.copy(
                        color = MaterialTheme.colors.onBackground,
                    ),
                )

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body2.copy(
                            color = MaterialTheme.colors.onBackground
                                .copy(alpha = subtitleAlpha),
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenButtonMaxPreview() {
    SalesAppTheme {
        HomeScreenButton(
            modifier = Modifier
                .width(maxButtonWidth)
                .height(minButtonHeight),
            title = "Button Title",
            subtitle = "Button subtitle",

            icon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                )
            }
        )
    }
}
@Preview
@Composable
private fun HomeScreenButtonMinPreview() {
    SalesAppTheme {
        HomeScreenButton(
            modifier = Modifier
                .width(minButtonWidth)
                .height(minButtonHeight),
            title = "Button Title",
            subtitle = "Button subtitle",

            icon = {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                )
            }
        )
    }
}