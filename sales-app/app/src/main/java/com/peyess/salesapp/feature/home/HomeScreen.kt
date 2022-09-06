package com.peyess.salesapp.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.peyess.salesapp.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    HomeScreenImpl(modifier = modifier)
}

@Preview
@Composable
private fun HomeScreenImpl(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_simple_empty)
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        LottieAnimation(
            modifier = modifier
                .height(240.dp)
                .width(240.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            // TODO: use string resource
            text = "Você não tem nenhuma notificação pendente",
            style = MaterialTheme.typography.h6
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        )
    }
}
