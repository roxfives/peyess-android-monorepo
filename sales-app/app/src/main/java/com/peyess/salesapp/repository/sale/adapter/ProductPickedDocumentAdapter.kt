package com.peyess.salesapp.repository.sale.adapter

import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument

fun ProductPickedDocument.toProductPickedEntity():ProductPickedEntity {
    return ProductPickedEntity(
        soId = soId,
        lensId = lensId,
        treatmentId = treatmentId,
        coloringId = coloringId,
    )
}
