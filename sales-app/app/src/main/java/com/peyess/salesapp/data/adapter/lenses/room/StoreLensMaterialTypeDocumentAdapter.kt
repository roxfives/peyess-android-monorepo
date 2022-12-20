package com.peyess.salesapp.data.adapter.lenses

import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialTypeEntity

fun StoreLensMaterialTypeDocument.toLocalLensMaterialTypeEntity(): LocalLensMaterialTypeEntity {
    return LocalLensMaterialTypeEntity(
        id = id,
        name = name,
    )
}