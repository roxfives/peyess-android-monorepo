package com.peyess.salesapp.utils.screen

import android.content.res.Resources
import timber.log.Timber
import kotlin.math.pow
import kotlin.math.sqrt

private const val diagonalThreshold = 1200.0

fun isScreenSizeLarge(): Boolean {
    val displayMetrics = Resources.getSystem().displayMetrics
    val width = displayMetrics.widthPixels / displayMetrics.density
    val height = displayMetrics.heightPixels / displayMetrics.density
    val diagonal = sqrt(width.pow(2) + height.pow(2))

    Timber.i("Screen size is $width x $height ($diagonal)")
    return diagonal >= diagonalThreshold
}