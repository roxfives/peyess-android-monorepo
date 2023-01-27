package com.peyess.salesapp.feature.sale.lens_comparison.utils

import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.lens_comparison.model.Prescription

fun animationFromCategory(categoryName: String, prescription: Prescription): Int {
    val name = categoryName.lowercase()

    // TODO: create name from type
    return if (
        name.contains("poly")
        || name.contains("1.59")
        || name.contains("trivex")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_myopia_85
        } else {
            R.raw.lottie_comparison_hipermetropia_85
        }
    } else if (
        name.contains("1.60")
        || name.contains("1.61")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_myopia_80
        } else {
            R.raw.lottie_comparison_hipermetropia_80
        }
    } else if (
        name.contains("1.67")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_myopia_65
        } else {
            R.raw.lottie_comparison_hipermetropia_65
        }
    } else if (
        name.contains("1.74")
        || name.contains("1.70")
        || name.contains("1.80")
        || name.contains("1.90")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_myopia_60
        } else {
            R.raw.lottie_comparison_hipermetropia_60
        }
    } else {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_myopia_base
        } else {
            R.raw.lottie_comparison_hipermetropia_base
        }
    }
}

fun bigAnimationFromCategory(categoryName: String, prescription: Prescription): Int {
    val name = categoryName.lowercase()

    // TODO: create name from type
    return if (
        name.contains("poly")
        || name.contains("1.59")
        || name.contains("trivex")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_big_myopia_85
        } else {
            R.raw.lottie_comparison_big_hipermetropia_85
        }
    } else if (
        name.contains("1.60")
        || name.contains("1.61")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_big_myopia_80
        } else {
            R.raw.lottie_comparison_big_hipermetropia_80
        }
    } else if (
        name.contains("1.67")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_big_myopia_65
        } else {
            R.raw.lottie_comparison_big_hipermetropia_65
        }
    } else if (
        name.contains("1.74")
        || name.contains("1.70")
        || name.contains("1.80")
        || name.contains("1.90")
    ) {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_big_myopia_60
        } else {
            R.raw.lottie_comparison_big_hipermetropia_60
        }
    } else {
        if (prescription.sphericalRight <= 0) {
            R.raw.lottie_comparison_big_myopia_base
        } else {
            R.raw.lottie_comparison_big_hipermetropia_base
        }
    }
}