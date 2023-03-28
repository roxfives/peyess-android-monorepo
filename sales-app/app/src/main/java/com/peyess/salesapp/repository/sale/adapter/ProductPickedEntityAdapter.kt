package com.peyess.salesapp.repository.sale.adapter

import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument

fun ProductPickedEntity.toProductPickedDocument(): ProductPickedDocument {
    return ProductPickedDocument(
        soId = soId,
        lensId = lensId,
        treatmentId = treatmentId,
        coloringId = coloringId,
    )
}