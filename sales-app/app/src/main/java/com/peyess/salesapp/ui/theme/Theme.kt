package com.peyess.salesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

private val DarkColorPalette = darkColors(
    primary = PrimaryDark, primaryVariant = PrimaryVariant, secondary = Secondary
)

private val LightColorPalette = lightColors(
    primary = PrimaryLight, primaryVariant = PrimaryVariant, secondary = Secondary

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PeyessTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current

    val dimensions = if (configuration.screenWidthDp <= 360) smallDimensions else sw360Dimensions

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    ProvideDimens(dimensions = dimensions) {
        MaterialTheme(
            colors = colors, typography = Typography, shapes = Shapes, content = content
        )
    }
}