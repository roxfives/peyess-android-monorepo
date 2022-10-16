package com.peyess.salesapp.feature.visual_acuity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import com.peyess.salesapp.R
import com.peyess.salesapp.utils.convertion.dpFrom

private val tableHeightMm = 152.8f
private val tableWidthMm = 91.2f

@Composable
fun VisualAcuityScreen(
    modifier: Modifier = Modifier,
) {
    VisualAcuityScreenImpl(modifier = modifier)
}

@Composable
private fun VisualAcuityScreenImpl(
    modifier: Modifier = Modifier,
) {
    val height = remember { dpFrom(tableHeightMm) }
    val width = remember { dpFrom(tableWidthMm) }

    val vector = ImageVector.vectorResource(id = R.drawable.visual_acuity_table)
    val painter = rememberVectorPainter(image = vector)

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier
                .height(height)
                .width(width),
            painter = painter,
            contentDescription = "",
        )
    }
}
