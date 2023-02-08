package com.peyess.salesapp.screen.sale.fee.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.screen.sale.fee.model.PaymentFee
import com.peyess.salesapp.screen.sale.fee.model.toPaymentFeeDocument
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.screen.sale.fee.model.toPaymentFee
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class PaymentFeeViewModel @AssistedInject constructor(
    @Assisted initialState: PaymentFeeState,
    private val paymentFeeRepository: PaymentFeeRepository,
): MavericksViewModel<PaymentFeeState>(initialState) {

    init {
        onEach(PaymentFeeState::saleId) { loadFee(it) }
        onEach(PaymentFeeState::currentPaymentFee) { calculatePricePreview(it) }

        onAsync(PaymentFeeState::currentFeeAsync) { processFeeResponse(it) }
    }

    private fun loadFee(saleId: String) {
        suspend {
            paymentFeeRepository.getPaymentFeeForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(currentFeeAsync = it)
        }
    }

    private fun processFeeResponse(response: PaymentFeeDocument?) = setState {
        if (response != null) {
            val fee = response.toPaymentFee()

            copy(currentPaymentFee = fee)
        } else {
            copy(
                currentFeeAsync = Fail(Throwable("No fee found"))
            )
        }
    }

    private fun updateFee(fee: PaymentFee) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            if (it.saleId.isNotBlank()) {
                val document = fee.toPaymentFeeDocument(it.saleId)

                paymentFeeRepository.updatePaymentFeeForSale(document)
            }
        }
    }

    private fun calculatePricePreview(fee: PaymentFee) = setState {
        val preview = when (fee.method) {
            PaymentFeeCalcMethod.Percentage ->
                originalPrice.multiply(
                    BigDecimal(1 + fee.value)
                        .setScale(4, RoundingMode.HALF_EVEN),
                )
            PaymentFeeCalcMethod.Whole ->
                originalPrice.plus(
                    BigDecimal(fee.value)
                        .setScale(2, RoundingMode.HALF_EVEN),
                )
            PaymentFeeCalcMethod.None ->
                originalPrice
        }

        copy(pricePreview = preview)
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

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setFullPrice(value: BigDecimal) = setState {
        copy(originalPrice = value)
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

        val update = limitToMaxFee(fee, originalPrice)

        updateFee(update)
        copy(currentPaymentFee = update)
    }

    fun onChangeFeeMethod(method: PaymentFeeCalcMethod) = setState {
        val fee = currentPaymentFee.copy(method = method)

        updateFee(fee)
        copy(currentPaymentFee = fee)
    }

    fun onFinished() = withState {
        suspend {
            val document = it.currentPaymentFee.toPaymentFeeDocument(it.saleId)

            paymentFeeRepository.updatePaymentFeeForSale(document)
        }.execute(Dispatchers.IO) {
            copy(hasFinished = it is Success)
        }
    }

    fun onNavigate() = setState {
        copy(hasFinished = false)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PaymentFeeViewModel, PaymentFeeState> {
        override fun create(state: PaymentFeeState): PaymentFeeViewModel
    }

    companion object:
        MavericksViewModelFactory<PaymentFeeViewModel, PaymentFeeState> by hiltMavericksViewModelFactory()
}