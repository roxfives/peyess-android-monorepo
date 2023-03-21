package com.peyess.salesapp.utils.screen

import android.content.res.Resources
import timber.log.Timber

private const val densityThreshold = 2.0

fun isHighResolution(): Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics

    Timber.i("Screen density is ${displayMetrics.density} (dpi: ${displayMetrics.densityDpi})")
    return displayMetrics.density >= densityThreshold
}