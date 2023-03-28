package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeCategoryEntity
import com.peyess.salesapp.data.model.lens.room.repo.LocalLensTypeDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeCategoryDocument

fun StoreLensTypeCategoryDocument.toLocalLensTypeCategoryEntity(): LocalLensTypeCategoryEntity {
    return LocalLensTypeCategoryEntity(
        id = id,
        name = name,
        explanation = explanation,
    )
}