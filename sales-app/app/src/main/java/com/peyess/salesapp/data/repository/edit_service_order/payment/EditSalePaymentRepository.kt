package com.peyess.salesapp.data.repository.edit_service_order.payment

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.data.model.edit_service_order.payment.EditSalePaymentDBView
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.InsertSalePaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.ReadSalePaymentError
import com.peyess.salesapp.data.repository.edit_service_order.payment.error.UpdateSalePaymentError
import kotlinx.coroutines.flow.Flow

typealias EditSalePaymentInsertResponse = Either<InsertSalePaymentError, Unit>

typealias EditSaleFetchResponse = Either<ReadSalePaymentError, List<SalePaymentDocument>>
typealias EditSalePaymentStreamResponse = Flow<EditSaleFetchResponse>

typealias EditSalePaymentUpdateResponse = Either<UpdateSalePaymentError, Unit>

interface EditSalePaymentRepository {
    suspend fun addPayment(payment: SalePaymentDocument): EditSalePaymentInsertResponse

    suspend fun paymentsForSale(saleId: String): EditSaleFetchResponse
    fun streamPaymentsForSale(saleId: String): EditSalePaymentStreamResponse

    suspend fun updateClientId(saleId: String, clientId: String): EditSalePaymentUpdateResponse
    suspend fun updateMethodId(saleId: String, methodId: String): EditSalePaymentUpdateResponse
    suspend fun updateMethodName(saleId: String, methodName: String): EditSalePaymentUpdateResponse
    suspend fun updateMethodType(saleId: String, methodType: String): EditSalePaymentUpdateResponse
    suspend fun updateValue(saleId: String, value: Double): EditSalePaymentUpdateResponse
    suspend fun updateInstallments(saleId: String, installments: Int): EditSalePaymentUpdateResponse
    suspend fun updateDocument(saleId: String, document: String): EditSalePaymentUpdateResponse
    suspend fun updateCardFlagName(saleId: String, cardFlagName: String): EditSalePaymentUpdateResponse
    suspend fun updateCardFlagIcon(saleId: String, cardFlagIcon: Uri): EditSalePaymentUpdateResponse
}