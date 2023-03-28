package com.peyess.salesapp.feature.prescription.utils

import com.peyess.salesapp.R
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

fun animationFor(categoryName: LensTypeCategoryName?): Int {
    return when (categoryName) {
        LensTypeCategoryName.Far -> R.raw.lottie_lens_far
        LensTypeCategoryName.Multi -> R.raw.lottie_lens_multi
        LensTypeCategoryName.Near -> R.raw.lottie_lens_near
        else -> R.raw.lottie_lens_far
    }
}