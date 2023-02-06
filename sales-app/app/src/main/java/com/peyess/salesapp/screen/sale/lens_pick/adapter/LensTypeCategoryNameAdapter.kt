package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.features.disponibility.contants.LensType

fun LensTypeCategoryName.toLensType(): LensType {
    return when(this) {
        LensTypeCategoryName.Far -> LensType.MonofocalFar
        LensTypeCategoryName.Near -> LensType.MonofocalNear
        LensTypeCategoryName.Multi -> LensType.MultiFocal
        LensTypeCategoryName.None -> LensType.None
    }
}
