package com.peyess.salesapp.app.adapter

import com.peyess.salesapp.dao.sale.active_so.db_view.ServiceOrderDBView
import com.peyess.salesapp.screen.home.model.UnfinishedSale

fun ServiceOrderDBView.toUnfinishedSale(): UnfinishedSale {
    return UnfinishedSale(
        saleId = saleId,
        serviceOrderId = serviceOrderId,
        clientName = clientName,
    )
}
