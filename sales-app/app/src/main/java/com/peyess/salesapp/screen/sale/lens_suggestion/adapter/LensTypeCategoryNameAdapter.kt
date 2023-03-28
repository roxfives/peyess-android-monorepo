package com.peyess.salesapp.screen.sale.lens_suggestion.adapter

import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.features.disponibility.contants.LensType

fun LensTypeCategoryName.toLensType(): LensType {
    return when(this) {
        LensTypeCategoryName.Far -> LensType.MonofocalFar
        LensTypeCategoryName.Near -> LensType.MonofocalNear
        LensTypeCategoryName.Multi -> LensType.MultiFocal
        LensTypeCategoryName.None -> LensType.None
    }
}
