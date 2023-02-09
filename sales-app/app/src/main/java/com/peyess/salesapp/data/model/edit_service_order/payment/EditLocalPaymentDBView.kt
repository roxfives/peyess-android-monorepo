package com.peyess.salesapp.data.model.edit_service_order.payment

import android.net.Uri
import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.local_client.LocalClientEntity

private const val paymentTable = EditLocalPaymentEntity.tableName
private const val clientsTable = LocalClientEntity.tableName

@DatabaseView(
    viewName = EditLocalPaymentDBView.viewName,
    value = """
        SELECT
            payment.id AS id, 
            payment.sale_id AS saleId, 
            payment.client_id AS clientId, 
            client.document AS clientDocument, 
            client.name AS clientName, 
            client.city AS clientCity, 
            client.state AS clientState, 
            payment.method_id AS methodId, 
            payment.method_name AS methodName, 
            payment.method_type AS methodType, 
            payment.value AS value, 
            payment.installments AS installments, 
            payment.document AS document, 
            payment.card_flag_name AS cardFlagName, 
            payment.card_flag_icon AS cardFlagIcon 
         FROM $paymentTable AS payment
         JOIN $clientsTable AS client ON payment.client_id = client.id
    """
)
data class EditLocalPaymentDBView(
    val id: Long = 0L,
    val saleId: String = "",
    val clientId: String = "",
    val clientDocument: String = "",
    val clientName: String = "",
    val clientCity: String = "",
    val clientState: String = "",
    val methodId: String = "",
    val methodName: String = "",
    val methodType: String = "",
    val value: Double = 0.0,
    val installments: Int = 1,
    val document: String = "",
    val cardFlagName: String = "",
    val cardFlagIcon: Uri = Uri.EMPTY,
) {
    companion object {
        const val viewName = "edit_payment_view"
    }
}
