package com.peyess.salesapp.data.adapter.lenses.room

import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTypeSimplified
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensTypeDocument

fun LocalLensesTypeSimplified.toStoreLensTypeDocument(): StoreLensTypeDocument {
    return StoreLensTypeDocument(
        id = id,
        name = name,
    )
}