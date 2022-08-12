package com.peyess.salesapp.feature.sale.prescription_data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight

@Composable
fun PrescriptionDataScreen() {

}


@Composable
private fun PrescriptionDataScreenImpl(
    modifier: Modifier = Modifier,

    mikeText: String = "",
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        MikeBubbleRight(
            text = mikeText,
        )
    }
}