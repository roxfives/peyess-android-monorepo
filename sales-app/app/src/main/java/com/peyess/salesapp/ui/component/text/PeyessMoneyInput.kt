package com.peyess.salesapp.ui.component.text

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.peyess.salesapp.ui.component.text.utils.currencyDigitsOnlyOrEmpty
import com.peyess.salesapp.ui.component.text.utils.percentageDigitsOnlyOrEmpty
import com.peyess.salesapp.ui.text_transformation.CurrencyVisualTransformation
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PeyessMoneyInput(
    modifier: Modifier = Modifier,
    value: BigDecimal = BigDecimal(0),
    onValueChanged: (value: BigDecimal) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = TextStyle.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val decimalFormat = remember {
        val formatter = NumberFormat.getInstance(Locale.US)

        formatter.minimumFractionDigits = 2
        formatter.maximumFractionDigits = 2
        formatter.minimumIntegerDigits = 1
        formatter.isGroupingUsed = false

        formatter
    }

    var textFieldValueState by remember {
        val formattedValue = try {
            decimalFormat.format(value)
        } catch (err: Throwable) {
            Timber.e("Failed to parse: $value", err)
            ""
        }

        val numbersOnly = formattedValue
            .replace("[^0-9]".toRegex(), "")
            .replace("^0*(?!$)".toRegex(), "")

        mutableStateOf( TextFieldValue(text = numbersOnly, selection = TextRange(9)) )
    }
    val textFieldValue = textFieldValueState.copy(
        text = textFieldValueState.text,
    )

    BasicTextField(
        modifier = modifier,

        value = currencyDigitsOnlyOrEmpty(value),
        onValueChange = {
//            textFieldValueState = it.copy(text = getDigitsOnlyOrEmpty(value))

            val updatedValue = try {
                BigDecimal(it)
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .divide(BigDecimal(100))
            } catch (err: Throwable) {
                Timber.e("Failed to parse: $it", err)
                BigDecimal(0.0)
            }

            onValueChanged(updatedValue)
        },

        visualTransformation = CurrencyVisualTransformation(),
        singleLine = true,

        keyboardOptions = keyboardOptions.copy(
            keyboardType = KeyboardType.Number,
        ),
        textStyle = textStyle,
        keyboardActions = keyboardActions,
    )
}