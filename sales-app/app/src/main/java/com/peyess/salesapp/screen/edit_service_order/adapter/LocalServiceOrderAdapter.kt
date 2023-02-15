package com.peyess.salesapp.screen.edit_service_order.adapter

import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.screen.edit_service_order.model.ServiceOrder

fun LocalServiceOrderDocument.toServiceOrder(): ServiceOrder {
    return ServiceOrder(
        id = id,
        hasPrescription = hasPrescription,
        saleId = saleId,
        clientName = clientName,
        lensTypeCategoryName = lensTypeCategoryName,
        isLensTypeMono = isLensTypeMono,
    )
}
