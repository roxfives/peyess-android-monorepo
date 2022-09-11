package com.peyess.salesapp.feature.sale.lens_comparison.utils

import com.peyess.salesapp.R

fun animationFromCategory(categoryName: String): Int {
    val name = categoryName.lowercase()

    // TODO: create name from type
    return if (
        name.contains("poly")
        || name.contains("1.59")
        || name.contains("trivex")
    ) {
        R.raw.lottie_comparison_myopia_85
    } else if (
        name.contains("1.60")
        || name.contains("1.61")
    ) {
        R.raw.lottie_comparison_myopia_80
    } else if (
        name.contains("1.67")
    ) {
        R.raw.lottie_comparison_myopia_65
    } else if (
        name.contains("1.74")
        || name.contains("1.70")
        || name.contains("1.80")
        || name.contains("1.90")
    ) {
        R.raw.lottie_comparison_myopia_60
    } else {
        R.raw.lottie_comparison_myopia_base
    }
}

fun bigAnimationFromCategory(categoryName: String): Int {
    val name = categoryName.lowercase()

    // TODO: create name from type
    return if (
        name.contains("poly")
        || name.contains("1.59")
        || name.contains("trivex")
    ) {
        R.raw.lottie_comparison_big_myopia_85
    } else if (
        name.contains("1.60")
        || name.contains("1.61")
    ) {
        R.raw.lottie_comparison_big_myopia_80
    } else if (
        name.contains("1.67")
    ) {
        R.raw.lottie_comparison_big_myopia_65
    } else if (
        name.contains("1.74")
        || name.contains("1.70")
        || name.contains("1.80")
        || name.contains("1.90")
    ) {
        R.raw.lottie_comparison_big_myopia_60
    } else {
        R.raw.lottie_comparison_big_myopia_base
    }
}