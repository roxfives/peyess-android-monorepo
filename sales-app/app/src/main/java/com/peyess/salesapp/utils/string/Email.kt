package com.peyess.salesapp.utils.string

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}