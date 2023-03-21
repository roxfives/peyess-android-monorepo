package com.peyess.salesapp.screen.sale.frames.landing.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.ConfigurationCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.frames.data.model.DisplayMeasure
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.NumberFormat

private val animationTitleSize = 128.dp
private val dialogPadding = 16.dp

private val measureColumnTitleSpacing = 16.dp
private val measureColumnSpacing = 32.dp
private val availabilityAnimationSize = 360.dp

private val availabilityMessageHeight = 16.dp

private val heightTitleSpacing = 42.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisplayMeasureDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    showMeasureLeft: Boolean = false,
    showMeasureRight: Boolean = false,

    measureLeft: DisplayMeasure = DisplayMeasure(),
    measureRight: DisplayMeasure = DisplayMeasure(),
) {
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(text = stringResource(id = R.string.dialog_value_btn_ok))
        },
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = !showMeasureLeft && !showMeasureRight,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            NoMeasureAvailable()
        }

        if (showMeasureLeft || showMeasureRight) {
            Column(
                modifier = Modifier
                    .padding(dialogPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                MeasureTitleAnimation()

                Spacer(modifier = Modifier.height(heightTitleSpacing))

                Row {
                    AnimatedVisibility(
                        visible = showMeasureRight,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        MeasureColumn(
                            title = stringResource(id = R.string.dialog_column_title_right),
                            measure = measureRight,
                        )
                    }

                    Spacer(modifier = Modifier.width(measureColumnSpacing))

                    AnimatedVisibility(
                        visible = showMeasureLeft,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        MeasureColumn(
                            title = stringResource(id = R.string.dialog_column_title_left),
                            measure = measureLeft,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MeasureTitleAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_ruler)
    )

    LottieAnimation(
        modifier = modifier.size(animationTitleSize),
        composition = composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Progress(0f, 1f),
    )
}

@Composable
private fun MeasureColumn(
    modifier: Modifier = Modifier,
    title: String = "",
    measure: DisplayMeasure = DisplayMeasure(),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(measureColumnTitleSpacing))

        Measure(
            title = stringResource(id = R.string.dialog_value_title_ipd),
            value = measure.ipd
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_height),
            value = measure.he
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_diameter),
            value = measure.diameter
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_bridge_hoop),
            value = measure.horizontalBridgeHoop
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_v_hoop),
            value = measure.vHoop
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_h_hoop),
            value = measure.hHoop
        )

        Measure(
            title = stringResource(id = R.string.dialog_value_title_bridge),
            value = measure.bridge
        )
    }
}

@Composable
private fun Measure(
    title: String = "",
    value: Double = 0.0,
) {
    val context = LocalContext.current
    val currentLocale = ConfigurationCompat
        .getLocales(context.resources.configuration)[0]

    val numberFormatter = if (currentLocale != null ) {
        NumberFormat.getNumberInstance(currentLocale)
    } else {
        NumberFormat.getNumberInstance()
    }
    numberFormatter.minimumFractionDigits = 2
    numberFormatter.maximumFractionDigits = 2
    numberFormatter.minimumIntegerDigits = 1

    val formattedValue = numberFormatter.format(value)
    Text(
        text = stringResource(id = R.string.dialog_value_display).format(title, formattedValue),
        style = MaterialTheme.typography.subtitle1,
    )
}

@Composable
private fun NoMeasureAvailable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_ruler)
        )

        LottieAnimation(
            modifier = Modifier.size(availabilityAnimationSize),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Spacer(modifier = Modifier.height(availabilityMessageHeight))

        Text(
            text = stringResource(id = R.string.dialog_message_not_available),
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
        )
    }
}