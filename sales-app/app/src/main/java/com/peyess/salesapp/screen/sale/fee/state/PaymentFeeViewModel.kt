package com.peyess.salesapp.screen.sale.fee.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.screen.sale.fee.model.PaymentFee
import com.peyess.salesapp.screen.sale.fee.model.toPaymentFeeDocument
import com.peyess.salesapp.repository.sale.SaleRepository
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
    private val saleRepository: SaleRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
): MavericksViewModel<PaymentFeeState>(initialState) {

    init {
     onEach(PaymentFeeState::saleId) {
            loadDiscount(it)
        }

        onEach(PaymentFeeState::fee) {
            calculatePricePreview(it)
        }
    }

    private fun loadDiscount(saleId: String) {
        paymentFeeRepository
            .watchPaymentFeeForSale(saleId)
            .execute { copy(currentFeeAsync = it) }
    }

    private fun updateFee(discount: PaymentFee) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            if (it.saleId.isNotBlank()) {
                val document = discount.toPaymentFeeDocument(it.saleId)

                val update = limitToMaxFee(
                    document,
                    it.originalPrice,
                )

                paymentFeeRepository.updatePaymentFeeForSale(update)
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

    private fun limitToMaxFee(
        paymentFee: PaymentFeeDocument,
        price: BigDecimal,
    ): PaymentFeeDocument {
        val fee = when (paymentFee.method) {
            PaymentFeeCalcMethod.None ->
                0.0
            PaymentFeeCalcMethod.Percentage -> {
                val maxFee = 1.0

                paymentFee.value.coerceAtMost(maxFee)
            }
            PaymentFeeCalcMethod.Whole -> {
                val maxFee = price.toDouble()

                paymentFee.value.coerceAtMost(maxFee)
            }
        }

        return paymentFee.copy(value = fee)
    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setFullPrice(value: BigDecimal) = setState {
        copy(originalPrice = value)
    }

    fun onChangeFeeValue(value: Double) = withState {
        val fee = when(it.fee.method) {
            PaymentFeeCalcMethod.Percentage ->
                it.fee.copy(percentValue = value)
            PaymentFeeCalcMethod.Whole ->
                it.fee.copy(wholeValue = value)
            PaymentFeeCalcMethod.None ->
                it.fee.copy()
        }

        updateFee(fee)
    }

    fun onChangeFeeMethod(method: PaymentFeeCalcMethod) = withState {
        val fee = it.fee.copy(method = method)

        updateFee(fee)
    }

    fun resetFee() {
        val discount = PaymentFee(
            method = PaymentFeeCalcMethod.Percentage,
            percentValue = 0.0,
            wholeValue = 0.0,
        )

        updateFee(discount)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PaymentFeeViewModel, PaymentFeeState> {
        override fun create(state: PaymentFeeState): PaymentFeeViewModel
    }

    companion object:
        MavericksViewModelFactory<PaymentFeeViewModel, PaymentFeeState> by hiltMavericksViewModelFactory()
}