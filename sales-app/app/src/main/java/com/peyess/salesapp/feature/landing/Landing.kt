package com.peyess.salesapp.feature.landing

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peyess.salesapp.R
import kotlinx.coroutines.delay

@Composable
fun Landing(
    modifier: Modifier = Modifier,
    onTimeout: () -> Unit
) {
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    val infiniteTransition = rememberInfiniteTransition()
    val scale = infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(true) {
        delay(500)
        currentOnTimeout()
    }

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(0.2f, false))
            Image(
                painterResource(id = R.drawable.ic_logo_peyess_dark_bg),
                contentDescription = null,
                modifier = Modifier
                    // TODO: fix dimension usage
                    .padding(all = 24.dp)
                    .weight(0.6f, false)
                    .scale(scale.value)
            )
            Spacer(modifier = Modifier.weight(0.2f, false))
        }
    }
}