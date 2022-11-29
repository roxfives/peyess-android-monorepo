package com.peyess.salesapp.ui.component.text

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.peyess.salesapp.ui.component.text.utils.percentageDigitsOnlyOrEmpty
import com.peyess.salesapp.ui.text_transformation.PercentVisualTransformation
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun PeyessPercentInput(
    modifier: Modifier = Modifier,
    value: BigDecimal = BigDecimal(0),
    onValueChanged: (value: BigDecimal) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    BasicTextField(
        modifier = modifier,

        value = percentageDigitsOnlyOrEmpty(value),
        onValueChange = {
            Timber.d("Current value in: $value")

            val updatedValue = try {
                BigDecimal(it)
                    .setScale(4)
                    .divide(BigDecimal(10000), RoundingMode.HALF_EVEN)
            } catch (err: Throwable) {
                Timber.e("Failed to parse: $it", err)
                BigDecimal(0.0)
            }

            onValueChanged(updatedValue)
        },

        visualTransformation = PercentVisualTransformation(),
        singleLine = true,

        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Number,
        ),
        textStyle = textStyle,
        keyboardActions = keyboardActions,
    )
}