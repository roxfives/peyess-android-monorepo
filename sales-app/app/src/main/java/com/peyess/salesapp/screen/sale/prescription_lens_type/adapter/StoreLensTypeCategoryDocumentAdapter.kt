package com.peyess.salesapp.screen.sale.prescription_lens_type.adapter

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument
import com.peyess.salesapp.screen.sale.prescription_lens_type.model.LensTypeCategory

fun StoreLensTypeCategoryDocument.toLensTypeCategory(): LensTypeCategory {
    return LensTypeCategory(
        id = id,
        name = name,
        explanation = explanation,
    )
}