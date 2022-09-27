package com.peyess.salesapp.feature.create_client.communication.util

import androidx.annotation.StringRes
import com.peyess.salesapp.R

@StringRes
fun validateEmail(email: String): Int? {
    if (email.isBlank()) {
        return R.string.create_client_error_email_empty
    }

    return null
}

@StringRes
fun validateCellphone(cellphone: String): Int? {
    if (cellphone.isBlank()) {
        return R.string.create_client_error_cellphone_empty
    }

    return null
}

@StringRes
fun validateWhatsapp(whatsapp: String): Int? {
    if (whatsapp.isBlank()) {
        return R.string.create_client_error_whatsapp_invalid
    }

    return null
}

@StringRes
fun validatePhone(phone: String): Int? {
    if (phone.isBlank()) {
        return R.string.create_client_error_cellphone_empty
    }

    return null
}