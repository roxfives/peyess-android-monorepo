package com.peyess.salesapp.utils.extentions

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun Context.activity(): ComponentActivity? {
    var currentContext = this

    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }

        currentContext = currentContext.baseContext
    }

    return null
}
