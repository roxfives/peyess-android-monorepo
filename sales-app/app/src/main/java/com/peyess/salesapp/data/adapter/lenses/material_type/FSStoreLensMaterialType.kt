package com.peyess.salesapp.data.adapter.lenses.material_type

import com.peyess.salesapp.data.model.lens.material_type.FSMaterialType
import com.peyess.salesapp.data.model.lens.material_type.StoreLensMaterialTypeDocument

fun FSMaterialType.toStoreLensMaterialTypeDocument(
    id: String,
): StoreLensMaterialTypeDocument {
    return StoreLensMaterialTypeDocument(
        id = id,
        name = name,
    )
}
