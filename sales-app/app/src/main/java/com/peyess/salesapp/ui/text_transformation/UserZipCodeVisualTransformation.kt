package com.peyess.salesapp.ui.text_transformation

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import timber.log.Timber

private const val breakPoint = 4

private const val maxLength = 8
//"13575-332"
class UserZipCodeVisualTransformation: VisualTransformation {
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

        if (textContent.isNotEmpty()) {
            firstEnding = breakPoint.coerceAtMost(textContent.length - 1)

            out += textContent.substring(0..firstEnding) + "-"
        }

        if (textContent.length > breakPoint + 1) {
            out += textContent.substring((breakPoint + 1) until textContent.length)
        }

        Timber.i("Out string is $out from origial $textContent")
        val numberOffsetTranslator = DocumentOffsetMapping()

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }

    private class DocumentOffsetMapping: OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            Timber.i("originalToTransformed: $offset")

            val offsetValue = if (offset <= breakPoint) {
                offset
            } else {
                offset + 1
            }

            return offsetValue.coerceAtMost(maxLength + 1)
        }

        override fun transformedToOriginal(offset: Int): Int {
            Timber.i("transformedToOriginal: $offset")

            val offsetValue = if (offset <= breakPoint + 1) {
                offset
            } else {
                offset - 1
            }

            return offsetValue.coerceAtMost(maxLength - 1)
        }
    }
}