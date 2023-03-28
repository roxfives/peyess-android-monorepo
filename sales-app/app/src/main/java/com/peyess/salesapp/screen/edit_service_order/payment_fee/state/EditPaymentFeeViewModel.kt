package com.peyess.salesapp.screen.edit_service_order.payment_fee.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.feature.payment_fee.model.PaymentFee
import com.peyess.salesapp.screen.sale.fee.adapter.toPaymentFee
import com.peyess.salesapp.screen.sale.fee.state.PaymentFeeState
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPaymentFeeViewModel, EditPaymentFeeState>
private typealias EditPaymentViewModelFactory =
        MavericksViewModelFactory<EditPaymentFeeViewModel, EditPaymentFeeState>

class EditPaymentFeeViewModel @AssistedInject constructor(
    @Assisted initialState: EditPaymentFeeState,
    private val paymentFeeRepository: EditPaymentFeeRepository,
): MavericksViewModel<EditPaymentFeeState>(initialState) {

    init {
        onEach(EditPaymentFeeState::saleId) { loadFee(it) }
        onEach(EditPaymentFeeState::currentPaymentFee) { calculatePricePreview(it) }

        onAsync(EditPaymentFeeState::paymentFeeResponseAsync) { processFeeResponse(it) }
    }

    private fun loadFee(saleId: String) {
        suspend {
            paymentFeeRepository.paymentFeeForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(paymentFeeResponseAsync = it)
        }
    }

    private fun processFeeResponse(response: EditPaymentFeeFetchResponse) = setState {
        response.fold(
            ifLeft = {
                copy(paymentFeeResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(currentPaymentFee = it.toPaymentFee())
            },
        )
    }

    private fun limitToMaxFee(paymentFee: PaymentFee, price: BigDecimal): PaymentFee {
        return when (paymentFee.method) {
            PaymentFeeCalcMethod.None ->
                paymentFee.copy()

            PaymentFeeCalcMethod.Percentage -> {
                val value = paymentFee.percentValue
                val maxFee = 1.0

                paymentFee.copy(percentValue = value.coerceAtMost(maxFee))
            }

            PaymentFeeCalcMethod.Whole -> {
                val value = paymentFee.wholeValue
                val maxFee = price.toDouble()

                paymentFee.copy(wholeValue = value.coerceAtMost(maxFee))
            }
        }
    }

    private fun calculatePricePreview(fee: PaymentFee) = setState {
        val preview = when (fee.method) {
            PaymentFeeCalcMethod.Percentage ->
                fullPrice.multiply(
                    BigDecimal(1 + fee.value)
                        .setScale(4, RoundingMode.HALF_EVEN),
                )
            PaymentFeeCalcMethod.Whole ->
                fullPrice.plus(
                    BigDecimal(fee.value)
                        .setScale(2, RoundingMode.HALF_EVEN),
                )
            PaymentFeeCalcMethod.None ->
                fullPrice
        }

        copy(pricePreview = preview)
    }

    fun onUpdateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onUpdateFullPrice(fullPrice: BigDecimal) = setState {
        copy(fullPrice = fullPrice)
    }

    fun onChangeFeeValue(value: Double) = setState {
        val fee = when(currentPaymentFee.method) {
            PaymentFeeCalcMethod.Percentage ->
                currentPaymentFee.copy(percentValue = value)
            PaymentFeeCalcMethod.Whole ->
                currentPaymentFee.copy(wholeValue = value)
            PaymentFeeCalcMethod.None ->
                currentPaymentFee.copy()
        }

        val update = limitToMaxFee(fee, fullPrice)
        viewModelScope.launch(Dispatchers.IO) {
            paymentFeeRepository.updateValue(saleId, update.value)
        }
        copy(currentPaymentFee = update)
    }

    fun onChangeFeeMethod(method: PaymentFeeCalcMethod) = setState {
        val fee = currentPaymentFee.copy(method = method)

        viewModelScope.launch(Dispatchers.IO) {
            paymentFeeRepository.updateMethod(saleId, method)
        }
        copy(currentPaymentFee = fee)
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditPaymentFeeState): EditPaymentFeeViewModel
    }

    companion object: EditPaymentViewModelFactory by hiltMavericksViewModelFactory()
}