package com.peyess.salesapp.data.adapter.edit_service_order.product_picked

import com.peyess.salesapp.data.model.edit_service_order.product_picked.EditProductPickedEntity
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument

fun ProductPickedDocument.toEditProductPicked(): EditProductPickedEntity {
    return EditProductPickedEntity(
        soId = soId,
        lensId = lensId,
        treatmentId = treatmentId,
        coloringId = coloringId,
    )
}
