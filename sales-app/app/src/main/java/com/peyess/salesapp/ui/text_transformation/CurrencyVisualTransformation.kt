package com.peyess.salesapp.ui.text_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.Locale

class CurrencyVisualTransformation(
    private val fixedCursorAtTheEnd: Boolean = true,
    private val numberOfDecimals: Int = 2,
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        val valueAsString = inputText.replace("[^0-9]".toRegex(), "")
        val value = BigDecimal(valueAsString.ifBlank { "0" })
            .divide(BigDecimal(100))
            .setScale(numberOfDecimals, RoundingMode.HALF_EVEN)

        val locale = Locale.getDefault()
        val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
        currencyFormatter.maximumFractionDigits = numberOfDecimals
        currencyFormatter.minimumFractionDigits = numberOfDecimals
        currencyFormatter.minimumIntegerDigits = 1

        val formattedNumber = currencyFormatter.format(value)

        Timber.d("Converting $inputText to $formattedNumber")

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
            return formatted.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            return unformatted.length
        }
    }

//    private class MovableCursorOffsetMapping(
//        private val unformatted: String = "",
//        private val formatted: String = "",
//    ) : OffsetMapping {
//        override fun originalToTransformed(offset: Int): Int {
//            val noLeadingZeros = unformatted
//                .replace("^0+(?!$)".toRegex(), "")
//
//            if (noLeadingZeros.length <= 1) {
//                return offset + 6
//            } else if (noLeadingZeros.length == 2) {
//                return offset + 5
//            } else if (unformatted.length - offset <= 2) {
//                val allSymbols = formatted.replace("[0-9]".toRegex(), "")
//
//                return offset + allSymbols.length - 1
//            } else {
//                val upToOffset = noLeadingZeros.subSequence(0, offset - 1)
//                val chunks = upToOffset.chunked(3)
//                val nonNumericSymbols = chunks.size - 1
//
//                return offset + nonNumericSymbols - 1
//            }
//
//
//
////            val allSymbols = formatted.replace("[^0-9]".toRegex(), "")
////
////            return offset //+ suffixLength + thousandSeparators(value) + endingZero(value)
//        }
//
//        override fun transformedToOriginal(offset: Int): Int {
//            val noLeadingZeros = unformatted
//                .replace("^0+(?!$)".toRegex(), "")
//
//            Timber.i("offset $offset for $unformatted with $noLeadingZeros to $formatted")
//
//            if (offset <= 4) {
//                return 1
//            } else if (unformatted.length - offset <= 2) {
//                val allSymbols = formatted.replace("[0-9]".toRegex(), "")
//
//                return offset - allSymbols.length - 1
//            } else {
//                val upToOffset = noLeadingZeros.subSequence(0, offset - 1)
//                val chunks = upToOffset.chunked(3)
//                val nonNumericSymbols = chunks.size
//
//                return offset - nonNumericSymbols
//            }
//        }
//
////        private fun thousandSeparators(value: Double): Int {
////            val total = log(value, 1000.0)
////                .coerceAtLeast(0.0)
////
////            return total.toInt()
////        }
////
////        private fun endingZero(value: Double): Int {
//////            val ending = value - truncate(value)
//////            val endingLength = ending.toString().length
//////
//////            return if (endingLength == 1) 1 else 0
////
////            return if (value == 0.0) 1 else 0
////        }
//    }
}