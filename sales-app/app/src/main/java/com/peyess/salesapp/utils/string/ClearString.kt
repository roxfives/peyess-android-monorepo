package com.peyess.salesapp.utils.string

import java.text.Normalizer

private val REGEX_DIACRITICS = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun CharSequence.removeDiacritics(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)

    return REGEX_DIACRITICS.replace(temp, "")
}

fun CharSequence.removePonctuation(): String {
    return replace("[^a-zA-Z\\d\\s]".toRegex(), "")
}
