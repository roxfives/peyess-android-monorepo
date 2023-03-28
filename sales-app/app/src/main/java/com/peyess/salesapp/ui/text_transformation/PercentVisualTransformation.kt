package com.peyess.salesapp.ui.text_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.peyess.salesapp.ui.component.text.utils.percentageDigitsOnlyOrEmpty
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class PercentVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val valueAsString = percentageDigitsOnlyOrEmpty(inputText)
        val value = BigDecimal(valueAsString.ifBlank { "0" })
            .divide(BigDecimal(10000))
            .setScale(4, RoundingMode.HALF_EVEN)

        val locale = Locale.getDefault()
        val percentFormatter = NumberFormat.getPercentInstance(locale)
        percentFormatter.maximumFractionDigits = 2
        percentFormatter.minimumFractionDigits = 2
        percentFormatter.minimumIntegerDigits = 1

        val formattedNumber = percentFormatter.format(value)

        Timber.d("Converting $inputText to $formattedNumber with value $value")

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles,
        )

        val offsetMapping = FixedCursorOffsetMapping(
            formatted = formattedNumber,
            unformatted = inputText,
        )

        return TransformedText(newText, offsetMapping)
    }

    private class FixedCursorOffsetMapping(
        private val formatted: String,
        private val unformatted: String,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return (formatted.length - 1).coerceAtLeast(1)
        }

        override fun transformedToOriginal(offset: Int): Int {
            return unformatted.length
        }
    }
}