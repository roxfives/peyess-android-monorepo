package com.peyess.salesapp.ui.text_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import timber.log.Timber

private const val firstBreakPoint = 0
private const val secondBreakPoint = 1
private const val thirdBreakPoint = 2
private const val fourthBreakPoint = 6

private const val maxLength = 11

class CellphoneNumberVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return maskFilter(text)
    }

    private fun maskFilter(text: AnnotatedString): TransformedText {
        val textContent = if (text.text.length <= maxLength) {
            text.text
        } else {
            text.text.substring(0 until maxLength)
        }

        var out = ""

        if (textContent.isNotEmpty()) {
            val ending = firstBreakPoint
                .coerceAtMost(textContent.length - 1)

            out += "(" + textContent.substring(0..ending)
        }

        if (textContent.length > firstBreakPoint + 1) {
            val ending = secondBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring((firstBreakPoint + 1)..ending) + ") "
        }

        if (textContent.length > secondBreakPoint) {
            val ending = thirdBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring((secondBreakPoint + 1)..ending) + " "
        }

        if (textContent.length > thirdBreakPoint) {
            val ending = fourthBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring((thirdBreakPoint + 1)..ending) + "-"
        }

        if (textContent.length > fourthBreakPoint) {
            out += textContent.substring((fourthBreakPoint + 1) until textContent.length)
        }

        Timber.i("Out string is $out from origial $textContent")
        val numberOffsetTranslator = DocumentOffsetMapping()

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }

    private class DocumentOffsetMapping: OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            Timber.i("originalToTransformed: $offset")

            val offsetValue = if (offset <= firstBreakPoint) {
                offset
            } else if (offset <= secondBreakPoint) {
                offset + 1
            } else if (offset <= thirdBreakPoint) {
                offset + 3
            } else if (offset <= fourthBreakPoint) {
                offset + 4
            } else {
                offset + 5
            }

            return offsetValue.coerceAtMost(maxLength + 5)
        }

        override fun transformedToOriginal(offset: Int): Int {
            Timber.i("transformedToOriginal: $offset")

            val offsetValue = if (offset <= firstBreakPoint + 1) {
                offset
            } else if (offset <= secondBreakPoint + 2) {
                offset - 1
            } else if (offset <= thirdBreakPoint + 3) {
                offset - 3
            } else if (offset <= fourthBreakPoint + 4) {
                offset - 4
            } else {
                offset - 5
            }

            return offsetValue.coerceAtMost(maxLength - 1)
        }
    }
}