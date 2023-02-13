package com.peyess.salesapp.dao.sale.active_so.db_view

import androidx.room.DatabaseView
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity

private const val saleTable = ActiveSalesEntity.tableName
private const val serviceOrderTable = ActiveSOEntity.tableName

@DatabaseView(
    viewName = ServiceOrderDBView.viewName,
    value = """
        SELECT
            sale.id AS saleId,
            sale.collaborator_uid AS collaboratorId,
            sale.active AS isActive,
            sale.finished AS hasFinished,
            service_order.id AS serviceOrderId,
            service_order.client_name AS clientName
         FROM $serviceOrderTable AS service_order
         JOIN $saleTable AS sale ON sale.id = service_order.sale_id
    """
)
data class ServiceOrderDBView(
    val saleId: String,
    val collaboratorId: String,
    val isActive: Boolean,
    val hasFinished: Boolean,
    val serviceOrderId: String,
    val clientName: String,
) {
    companion object {
        const val viewName = "service_order_db_view"
    }
}
