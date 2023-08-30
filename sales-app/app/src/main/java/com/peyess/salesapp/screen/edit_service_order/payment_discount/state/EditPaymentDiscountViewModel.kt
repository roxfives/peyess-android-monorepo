package com.peyess.salesapp.screen.edit_service_order.payment_discount.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.purchase.DiscountGroupRepository
import com.peyess.salesapp.feature.payment_discount.model.Discount
import com.peyess.salesapp.feature.payment_discount.model.group.DiscountGroup
import com.peyess.salesapp.feature.payment_discount.model.group.toDiscountGroup
import com.peyess.salesapp.feature.payment_discount.model.toDiscount
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.screen.sale.discount.state.DiscountState
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPaymentDiscountViewModel, EditPaymentDiscountState>
private typealias EditPaymentDiscountViewModelFactory =
        MavericksViewModelFactory<EditPaymentDiscountViewModel, EditPaymentDiscountState>

class EditPaymentDiscountViewModel @AssistedInject constructor(
    @Assisted initialState: EditPaymentDiscountState,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val discountGroupRepository: DiscountGroupRepository,
    private val discountRepository: EditPaymentDiscountRepository,
): MavericksViewModel<EditPaymentDiscountState>(initialState) {

    init {
        loadDiscountGroup()

        onEach(EditPaymentDiscountState::saleId) { loadDiscount(it) }
        onEach(EditPaymentDiscountState::currentDiscount) { calculatePricePreview(it) }

        onAsync(EditPaymentDiscountState::discountGroupDocumentAsync) {
            processDiscountGroupResponse(it)
        }
        onAsync(EditPaymentDiscountState::discountResponseAsync) { processDiscountResponse(it) }
    }

    private fun loadDiscountGroup() {
        suspend {
            val uid = authenticationRepository.fetchCurrentUserId()
            val collaborator = collaboratorsRepository.getById(uid)

            discountGroupRepository
                .getById(collaborator?.storeDiscountGroup ?: "")
        }.execute {
            copy(discountGroupDocumentAsync = it)
        }
    }

    private fun processDiscountGroupResponse(response: DiscountGroupDocument?) = setState {
        if (response != null) {
            copy(discountGroup = response.toDiscountGroup())
        } else {
            copy(
                discountGroupDocumentAsync = Fail(Throwable("Discount group not found"))
            )
        }
    }

    private fun loadDiscount(saleId: String) {
        suspend {
            discountRepository.discountForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(discountResponseAsync = it)
        }
    }

    private fun processDiscountResponse(
        response: EditPaymentDiscountFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(discountResponseAsync = Fail(it.error))
            },

            ifRight = {
                copy(currentDiscount = it.toDiscount())
            }
        )
    }

    private fun calculatePricePreview(discount: Discount) = setState {
        val preview = when (discount.method) {
            DiscountCalcMethod.Percentage ->
                fullPrice.multiply(
                    (BigDecimal.ONE - discount.value)
                        .setScale(4, RoundingMode.HALF_EVEN),
                )
            DiscountCalcMethod.Whole ->
                fullPrice.minus(
                    discount.value.setScale(2, RoundingMode.HALF_EVEN),
                )
            DiscountCalcMethod.None ->
                fullPrice
        }

        copy(pricePreview = preview)
    }

    private fun limitToMaxDiscount(
        discount: Discount,
        discountGroup: DiscountGroup,
        price: BigDecimal,
    ): Discount {
        return when (discount.method) {
            DiscountCalcMethod.None ->
                discount.copy()

            DiscountCalcMethod.Percentage -> {
                val maxDiscount = discountGroup.general
                    .maxValueAsPercentage(price)

                val limited = discount.percentValue
                    .coerceAtMost(maxDiscount)

                discount.copy(percentValue = limited)
            }

            DiscountCalcMethod.Whole -> {
                val maxDiscount = discountGroup.general
                    .maxValueAsWhole(price)

                val limited = discount.wholeValue
                    .coerceAtMost(maxDiscount)

                discount.copy(wholeValue = limited)
            }
        }
    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setFullPrice(value: BigDecimal) = setState {
        copy(fullPrice = value)
    }

    fun onChangeDiscountValue(value: BigDecimal) = setState {
        val updatedValue = value.setScale(4, RoundingMode.HALF_EVEN)

        val discount = when(currentDiscount.method) {
            DiscountCalcMethod.Percentage ->
                currentDiscount.copy(percentValue = updatedValue)
            DiscountCalcMethod.Whole ->
                currentDiscount.copy(wholeValue = updatedValue)
            DiscountCalcMethod.None ->
                currentDiscount.copy()
        }

        val update = limitToMaxDiscount(
            discount = discount,
            discountGroup = discountGroup,
            price = fullPrice,
        )
        viewModelScope.launch(Dispatchers.IO) {
            discountRepository.updateValue(saleId, update.value)
        }
        copy(currentDiscount = update)
    }

    fun onChangeDiscountMethod(method: DiscountCalcMethod) = setState {
        val discount = currentDiscount.copy(method = method)

        viewModelScope.launch(Dispatchers.IO) { discountRepository.updateMethod(saleId, method) }
        copy(currentDiscount = discount)
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditPaymentDiscountState): EditPaymentDiscountViewModel
    }

    companion object: EditPaymentDiscountViewModelFactory by hiltMavericksViewModelFactory()
}