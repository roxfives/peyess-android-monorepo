package com.peyess.salesapp.screen.sale.lens_pick.adapter

import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.features.disponibility.model.AlternativeHeight

fun StoreLensAltHeightDocument.toAlternativeHeight(): AlternativeHeight {
    return AlternativeHeight(value = value)
}