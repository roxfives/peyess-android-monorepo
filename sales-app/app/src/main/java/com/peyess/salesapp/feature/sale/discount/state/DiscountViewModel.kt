package com.peyess.salesapp.feature.sale.discount.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.purchase.DiscountGroupRepository
import com.peyess.salesapp.feature.sale.discount.model.Discount
import com.peyess.salesapp.feature.sale.discount.model.group.DiscountGroup
import com.peyess.salesapp.feature.sale.discount.model.toOverallDiscountDocument
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class DiscountViewModel @AssistedInject constructor(
    @Assisted initialState: DiscountState,
    private val saleRepository: SaleRepository,
    private val discountRepository: OverallDiscountRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
    private val discountGroupRepository: DiscountGroupRepository,
    private val authenticationRepository: AuthenticationRepository,
): MavericksViewModel<DiscountState>(initialState) {

    init {
        loadDiscountGroup()

        onEach(DiscountState::saleId) {
            loadDiscount(it)
        }

        onEach(DiscountState::discount) {
            calculatePricePreview(it)
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

    private fun loadDiscount(saleId: String) {
        discountRepository
            .watchDiscountForSale(saleId)
            .execute { copy(currentDiscountAsync = it) }
    }

    private fun updateDiscount(discount: Discount) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            if (it.saleId.isNotBlank()) {
                val document = discount
                    .toOverallDiscountDocument(it.saleId)

                val update = limitToMaxDiscount(
                    document,
                    it.discountGroup,
                    it.originalPrice,
                )

                discountRepository.updateDiscountForSale(update)
            }
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
        overallDiscount: OverallDiscountDocument,
        discountGroup: DiscountGroup,
        price: BigDecimal,
    ): OverallDiscountDocument {
        val discount = when (overallDiscount.discountMethod) {
            DiscountCalcMethod.None ->
                0.0
            DiscountCalcMethod.Percentage -> {
                val maxDiscount = discountGroup.general
                    .maxValueAsPercentage(price)
                    .toDouble()

                overallDiscount.overallDiscountValue
                    .coerceAtMost(maxDiscount)
            }
            DiscountCalcMethod.Whole -> {
                val maxDiscount = discountGroup.general
                    .maxValueAsWhole(price)
                    .toDouble()

                overallDiscount.overallDiscountValue
                    .coerceAtMost(maxDiscount)
            }
        }

        return overallDiscount.copy(overallDiscountValue = discount)
    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setFullPrice(value: BigDecimal) = setState {
        copy(originalPrice = value)
    }

    fun onChangeDiscountValue(value: Double) = withState {
        val discount = when(it.discount.method) {
            DiscountCalcMethod.Percentage ->
                it.discount.copy(percentValue = value)
            DiscountCalcMethod.Whole ->
                it.discount.copy(wholeValue = value)
            DiscountCalcMethod.None ->
                it.discount.copy()
        }

        updateDiscount(discount)
    }

    fun onChangeDiscountMethod(method: DiscountCalcMethod) = withState {
        val discount = it.discount.copy(method = method)

        updateDiscount(discount)
    }

    fun resetDiscount() {
        val discount = Discount(
            method = DiscountCalcMethod.None,
            percentValue = 0.0,
            wholeValue = 0.0,
        )

        updateDiscount(discount)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<DiscountViewModel, DiscountState> {
        override fun create(state: DiscountState): DiscountViewModel
    }

    companion object:
        MavericksViewModelFactory<DiscountViewModel, DiscountState> by hiltMavericksViewModelFactory()
}