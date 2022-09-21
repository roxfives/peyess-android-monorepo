package com.peyess.salesapp.feature.create_client.basic_info.utils

import androidx.annotation.StringRes
import com.peyess.salesapp.R
import java.time.LocalDate
import java.time.ZonedDateTime

@StringRes
fun validateName(name: String): Int? {
    val blankSpaceRegex = "\\s+".toRegex()
    val words = name.split(
        regex = blankSpaceRegex,
        limit = 2,
    )



    if (name.isBlank()) {
        return R.string.error_client_name_empty
    } else if (words.size <= 1) {
        return R.string.create_client_error_name_incomplete
    }

    return null
}

@StringRes
fun validateNameDisplay(name: String): Int? {
    if (name.isBlank()) {
        return R.string.create_client_error_cpf_blank
    }

    return null
}

@StringRes
fun validateDocument(document: String): Int? {
    if (document.isBlank()) {
        return R.string.create_client_error_cpf_invalid
    }

    return null
}

@StringRes
fun validateBirthDate(date: ZonedDateTime): Int? {
    if (date.isAfter(ZonedDateTime.now())) {
        return R.string.create_client_error_birthday_invalid
    }

    return null
}