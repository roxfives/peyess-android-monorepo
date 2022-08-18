package com.peyess.salesapp.feature.sale.frames_measure

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import timber.log.Timber

@Composable
fun LandingAnimationHelperScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onNext: (eye: Eye) -> Unit = {},
) {
    val eye = remember { mutableStateOf<Eye?>(Eye.None)}
    val eyeParameter = navHostController.currentBackStackEntry
        ?.arguments
        ?.getString("eye")

    LaunchedEffect(eyeParameter) {
        Timber.i("Using eye $eyeParameter")
        eye.value = if (eyeParameter == "left") Eye.Left else Eye.Right
    }

    when (eye.value) {
        is Eye.None, null ->
            PeyessProgressIndicatorInfinite()
        else ->
            LandingAnimationHelperImpl(
                modifier = modifier,
                eye = eye.value!!,
                onClick = { onNext(eye.value!!) },
            )
    }
}

@Composable
private fun LandingAnimationHelperImpl(
    modifier: Modifier = Modifier,
    eye: Eye = Eye.Right,
    onClick: () -> Unit = {},
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (eye) {
                Eye.Right ->
                    R.raw.lottie_measuring_adjust_right
                else ->
                    R.raw.lottie_measuring_adjust_left
            }
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
            modifier = modifier
                .weight(0.6f, false)
                .fillMaxSize()
        )

        // TODO(Fix dimensions)
        ProvideTextStyle(value = MaterialTheme.typography.h6) {
            Text(
                text = stringResource(id = R.string.measure_picture_adjustment),
                Modifier.padding(8.dp)
            )
        }

        IconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(16.dp)
                .size(60.dp)
                .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape),
        ) {
            Icon(Icons.Default.Done, contentDescription = null)
        }
    }
}