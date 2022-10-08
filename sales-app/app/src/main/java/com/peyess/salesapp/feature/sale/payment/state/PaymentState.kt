package com.peyess.salesapp.feature.sale.payment.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import timber.log.Timber

data class PaymentState(
    val paymentMethodsAsync: Async<List<PaymentMethod>> = Uninitialized,
    val activePaymentMethod: PaymentMethod? = null,

    val clientAsync: Async<ClientDocument?> = Uninitialized,

    val paymentAsync: Async<SalePaymentEntity?> = Uninitialized,

    val cardFlagsAsync: Async<List<CardFlagDocument>> = Uninitialized,

    val soAsync: Async<ActiveSOEntity?> = Uninitialized,
    val totalToPayAsync: Async<Double> = Uninitialized,
    val totalPaidAsync: Async<Double> = Uninitialized,

    val totalLeftToPay: Double = 0.0,
): MavericksState {
    val arePaymentsLoading = paymentMethodsAsync is Loading
    val paymentMethods = if (paymentMethodsAsync is Success) {
        Timber.i("Setting payment methods ${paymentMethodsAsync.invoke()}")
        paymentMethodsAsync.invoke()
    } else {
        Timber.i("Setting empty payment methods")
        emptyList()
    }

    val areCardFlagsLoading = cardFlagsAsync is Loading
    val cardFlags = cardFlagsAsync.invoke() ?: emptyList()

    val isClientLoading = clientAsync is Loading
    val client = if (clientAsync is Success && clientAsync.invoke() != null) {
        clientAsync.invoke()!!
    } else {
        ClientDocument()
    }

    val isPaymentLoading = paymentAsync is Loading
    val payment = if (paymentAsync is Success && paymentAsync.invoke() != null) {
        paymentAsync.invoke()!!
    } else {
        SalePaymentEntity()
    }
}