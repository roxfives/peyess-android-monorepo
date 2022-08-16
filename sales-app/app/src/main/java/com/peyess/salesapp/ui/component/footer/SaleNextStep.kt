package com.peyess.salesapp.ui.component.footer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.peyess.salesapp.R
import com.peyess.salesapp.ui.theme.SalesAppTheme

@Composable
fun PeyessNextStep(
    modifier: Modifier = Modifier,
    startButton: @Composable () -> Unit = {},

    isLoadingConstraints: Boolean = false,
    canGoNext: Boolean = true,
    onNext: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
    ) {
        startButton()

        Spacer(modifier = Modifier.weight(1f))

        if (isLoadingConstraints) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(SalesAppTheme.dimensions.minimum_touch_target),
            )
        } else {
            Button(
                modifier = Modifier
                    .height(SalesAppTheme.dimensions.minimum_touch_target),
                enabled = canGoNext,
                onClick = onNext,
            ) {
                Text(text = stringResource(id = R.string.go_next_default))
            }
        }
    }
}