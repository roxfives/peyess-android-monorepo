package com.peyess.salesapp.data.adapter.internal.utils

import com.peyess.salesapp.typing.lens.LensTypeCategoryName

fun lensTypeName(lensType: LensTypeCategoryName): String {
    return when(lensType) {
        LensTypeCategoryName.Far -> "far"
        LensTypeCategoryName.Multi -> "multi"
        LensTypeCategoryName.Near -> "near"
        LensTypeCategoryName.None -> "none"
    }
}