package com.peyess.salesapp.data.adapter.edit_service_order.sale

import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity

fun LocalSaleDocument.toEditSaleEntity(): EditSaleEntity {
    return EditSaleEntity(
        id = id,
        collaboratorUid = collaboratorUid,
        active = active,
        isUploading = isUploading,
    )
}
