package com.peyess.salesapp.data.adapter.lenses.lens_type_category

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeCategoryEntity
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument

fun LocalLensTypeCategoryEntity.toStoreLensTypeCategoryDocument(): StoreLensTypeCategoryDocument {
    return StoreLensTypeCategoryDocument(
        id = id,
        name = name,
        explanation = explanation,
    )
}