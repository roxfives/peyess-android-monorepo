package com.peyess.salesapp.data.adapter.edit_service_order.service_order

import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity

fun EditServiceOrderEntity.toServiceOrderDocument(): LocalServiceOrderDocument {
    return LocalServiceOrderDocument(
        id = id,
        hasPrescription = hasPrescription,
        saleId = saleId,
        clientName = clientName,
    )
}
