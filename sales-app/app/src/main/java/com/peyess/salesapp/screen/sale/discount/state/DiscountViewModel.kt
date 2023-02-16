package com.peyess.salesapp.screen.sale.discount.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.purchase.DiscountGroupRepository
import com.peyess.salesapp.screen.sale.discount.model.Discount
import com.peyess.salesapp.screen.sale.discount.model.group.DiscountGroup
import com.peyess.salesapp.screen.sale.discount.model.toOverallDiscountDocument
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.screen.sale.discount.model.group.toDiscountGroup
import com.peyess.salesapp.screen.sale.discount.model.toDiscount
import com.peyess.salesapp.screen.sale.fee.adapter.toPaymentFeeDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class DiscountViewModel @AssistedInject constructor(
    @Assisted initialState: DiscountState,
    private val discountRepository: OverallDiscountRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val discountGroupRepository: DiscountGroupRepository,
    private val authenticationRepository: AuthenticationRepository,
): MavericksViewModel<DiscountState>(initialState) {

    init {
        loadDiscountGroup()

        onEach(DiscountState::saleId) { loadDiscount(it) }
        onEach(DiscountState::currentDiscount) { calculatePricePreview(it) }

        onAsync(DiscountState::discountGroupDocumentAsync) { processDiscountGroupResponse(it) }
        onAsync(DiscountState::currentDiscountAsync) { processDiscountResponse(it) }
    }

    private fun updateDiscount(discount: Discount) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            if (it.saleId.isNotBlank()) {
                val document = discount.toOverallDiscountDocument(it.saleId)

                discountRepository.updateDiscountForSale(document)
            }
        }
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
            discountRepository.getDiscountForSale(saleId)
        }.execute(Dispatchers.IO) {
            copy(currentDiscountAsync = it)
        }
    }

    private fun processDiscountResponse(response: OverallDiscountDocument?) = setState {
        if (response != null) {
            copy(currentDiscount = response.toDiscount())
        } else {
            copy(
                currentDiscountAsync = Fail(Throwable("Discount not found"))
            )
        }
    }

    private fun calculatePricePreview(discount: Discount) = setState {
        val preview = when (discount.method) {
            DiscountCalcMethod.Percentage ->
                originalPrice.multiply(
                    BigDecimal(1.0 - discount.value)
                        .setScale(4, RoundingMode.HALF_EVEN),
                )
            DiscountCalcMethod.Whole ->
                originalPrice.minus(
                    BigDecimal(discount.value)
                        .setScale(2, RoundingMode.HALF_EVEN),
                )
            DiscountCalcMethod.None ->
                originalPrice
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
                    .toDouble()

                val limited = discount.percentValue
                    .coerceAtMost(maxDiscount)

                discount.copy(percentValue = limited)
            }

            DiscountCalcMethod.Whole -> {
                val maxDiscount = discountGroup.general
                    .maxValueAsWhole(price)
                    .toDouble()

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
        copy(originalPrice = value)
    }

    fun onChangeDiscountValue(value: Double) = setState {
        val discount = when(currentDiscount.method) {
            DiscountCalcMethod.Percentage ->
                currentDiscount.copy(percentValue = value)
            DiscountCalcMethod.Whole ->
                currentDiscount.copy(wholeValue = value)
            DiscountCalcMethod.None ->
                currentDiscount.copy()
        }

        val update = limitToMaxDiscount(
            discount,
            discountGroup,
            originalPrice,
        )

        updateDiscount(update)
        copy(currentDiscount = update)
    }

    fun onChangeDiscountMethod(method: DiscountCalcMethod) = setState {
        val discount = currentDiscount.copy(method = method)

        updateDiscount(discount)
        copy(currentDiscount = discount)
    }

    fun onFinished() = withState {
        suspend {
            val document = it.currentDiscount.toOverallDiscountDocument(it.saleId)

            discountRepository.updateDiscountForSale(document)
        }.execute(Dispatchers.IO) {
            copy(hasFinished = it is Success)
        }
    }

    fun onNavigate() = setState {
        copy(hasFinished = false)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<DiscountViewModel, DiscountState> {
        override fun create(state: DiscountState): DiscountViewModel
    }

    companion object:
        MavericksViewModelFactory<DiscountViewModel, DiscountState> by hiltMavericksViewModelFactory()
}