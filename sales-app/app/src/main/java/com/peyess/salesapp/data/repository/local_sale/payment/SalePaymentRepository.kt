package com.peyess.salesapp.data.repository.local_sale.payment

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentDocument
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentReadError
import com.peyess.salesapp.data.repository.local_sale.payment.error.SalePaymentWriteError

typealias SalePaymentResponse = Either<SalePaymentReadError, List<SalePaymentDocument>>
typealias SalePaymentTotalResponse = Either<SalePaymentReadError, Double>
typealias SalePaymentWriteResult = Either<SalePaymentWriteError, Long>
typealias SalePaymentUpdateResult = Either<SalePaymentWriteError, Unit>

interface SalePaymentRepository {
    fun paymentForSale(saleId: String): SalePaymentResponse

    fun totalPaymentForSale(saleId: String): SalePaymentTotalResponse

    fun addPaymentToSale(paymentDocument: SalePaymentDocument): SalePaymentWriteResult

    fun updatePayment(paymentDocument: SalePaymentDocument): SalePaymentUpdateResult
}
