package com.peyess.salesapp.ui.text_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import timber.log.Timber

private const val firstBreakPoint = 2
private const val secondBreakPoint = 5
private const val thirdBreakPoint = 8

private const val maxLength = 11

class UserDocumentVisualTransformation: VisualTransformation {
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

        val firstEnding: Int
        val secondEnding: Int
        val thirdEnding: Int

        if (textContent.isNotEmpty()) {
            firstEnding = firstBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring(0..firstEnding) + "."
        }

        if (textContent.length > firstBreakPoint) {
            secondEnding = secondBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring((firstBreakPoint + 1)..secondEnding) + "."
        }

        if (textContent.length > secondBreakPoint) {
            thirdEnding = thirdBreakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring((secondBreakPoint + 1)..thirdEnding) + "-"
        }

        if (textContent.length > thirdBreakPoint) {
            out += textContent.substring((thirdBreakPoint + 1) until textContent.length)
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
                offset + 2
            } else {
                offset + 3
            }

            return offsetValue.coerceAtMost(maxLength + 3)
        }

        override fun transformedToOriginal(offset: Int): Int {
            Timber.i("transformedToOriginal: $offset")

            val offsetValue = if (offset <= firstBreakPoint + 1) {
                offset
            } else if (offset <= secondBreakPoint + 2) {
                offset - 1
            } else if (offset <= thirdBreakPoint + 3) {
                offset - 2
            } else {
                offset - 3
            }

            return offsetValue.coerceAtMost(maxLength - 1)
        }
    }
}