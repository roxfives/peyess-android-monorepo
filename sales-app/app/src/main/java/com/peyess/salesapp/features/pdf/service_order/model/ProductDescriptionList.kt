package com.peyess.salesapp.features.pdf.service_order.model

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.payment_fee.toWholeFormat
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.toWholeFormat
import com.peyess.salesapp.features.pdf.service_order.utils.calculateDiscount
import com.peyess.salesapp.features.pdf.service_order.utils.joinDiscounts
import com.peyess.salesapp.features.pdf.service_order.utils.joinFees
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.utils.extentions.transformAll
import com.peyess.salesapp.utils.extentions.transformItem
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

class ProductDescriptionList {
    private val priceThreshold = BigDecimal("0.01")

    private val productDescriptionList: MutableList<ProductDescription> = mutableListOf()

    fun addProductDescription(productDescription: ProductDescription) {
        val itemExists = productDescriptionList.isNotEmpty()
                && productDescriptionList.find {
                    it.description == productDescription.description
                } != null

        if (!itemExists) {
            productDescriptionList.add(productDescription)
        } else {
            productDescriptionList.transformItem({
                it.description == productDescription.description
            }) {
                val discount = joinDiscounts(
                    a = it.discount,
                    b = productDescription.discount,
                    priceWithoutDiscount = it.priceWithoutDiscount,
                )
                val priceWithDiscount = calculateDiscount(
                    discount = discount,
                    priceWithoutDiscount = it.priceWithoutDiscount,
                )

                it.copy(
                    quantity = it.quantity + productDescription.quantity,
                    priceWithoutDiscount = it.priceWithoutDiscount + productDescription.priceWithoutDiscount,
                    discount = discount,
                    fee = joinFees(
                        a = it.fee,
                        b = productDescription.fee,
                        priceWithDiscount = priceWithDiscount,
                    )
                )
            }
        }
    }

    fun totalWholeDiscount(): BigDecimal {
        return productDescriptionList
            .ifEmpty { return BigDecimal.ZERO }
            .map { it.discount.toWholeFormat(it.priceWithoutDiscount).value }
            .reduce(BigDecimal::add)
    }

    fun totalWholeFee(): BigDecimal {
        return productDescriptionList
            .ifEmpty { return BigDecimal.ZERO }
            .map { it.fee.toWholeFormat(it.priceWithDiscount).value }
            .reduce(BigDecimal::add)
    }

    fun totalPercentageFee(): BigDecimal {
        val totalPriceWithDiscount = productDescriptionList
            .ifEmpty { return BigDecimal.ZERO }
            .map { it.priceWithDiscount }
            .reduce(BigDecimal::add)
        val wholeFee = totalWholeFee()

        return wholeFee
            .divide(totalPriceWithDiscount)
            .setScale(4, RoundingMode.HALF_EVEN)
    }

    fun priceWithoutDiscount(): BigDecimal {
        return productDescriptionList
            .ifEmpty { return BigDecimal.ZERO }
            .map { it.priceWithoutDiscount }
            .reduce(BigDecimal::add)
    }

    fun finalPrice(): BigDecimal {
        return productDescriptionList
            .ifEmpty { return BigDecimal.ZERO }
            .map {
                it.priceWithoutDiscount
                    .subtract(it.discount.toWholeFormat(it.priceWithoutDiscount).value)
                    .add(it.fee.toWholeFormat(it.priceWithDiscount).value)
            }.reduce(BigDecimal::add)
    }

    fun productListDescription(context: Context): String {
        return productDescriptionList.map {
            productUnitDescription(
                context = context,
                productCode = it.productCode,
                description = it.description,
                quantity = it.quantity,
                discount = it.discount.toWholeFormat(it.priceWithoutDiscount).value
                    .setScale(2, RoundingMode.HALF_EVEN),
                fee = it.fee.toWholeFormat(it.priceWithDiscount).value
                    .setScale(2, RoundingMode.HALF_EVEN),

                priceWithoutDiscount = it.priceWithoutDiscount
                    .setScale(2, RoundingMode.HALF_EVEN),
                priceWithFee = it.priceWithFee
                    .setScale(2, RoundingMode.HALF_EVEN),
            )
        }.ifEmpty { return "" }
        .reduce(String::plus)
    }

    fun applyDiscountOverall(discount: DiscountDescriptionDocument) {
        when (discount.method) {
            is DiscountCalcMethod.None -> {
                productDescriptionList.transformAll {
                    it.copy(
                        discount = discount.copy(value = BigDecimal.ZERO),
                    )
                }
            }

            is DiscountCalcMethod.Percentage -> {
                productDescriptionList.transformAll {
                    it.copy(
                        discount = discount.copy(value = BigDecimal.ZERO),
                    )
                }

                val totalPrice = priceWithoutDiscount()
                val discountAsWhole = discount.toWholeFormat(totalPrice)

                applyWholeDiscountOverall(discountAsWhole)
            }

            is DiscountCalcMethod.Whole -> {
                applyWholeDiscountOverall(discount)
            }
        }
    }

    private fun applyWholeDiscountOverall(discount: DiscountDescriptionDocument) {
        val totalItems = productDescriptionList
            .map { it.quantity }
            .ifEmpty { listOf(0) }
            .reduce(Int::plus)
            .toBigDecimal()

        val discountPerProduct = discount.value
            .divide(totalItems, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN)

        var delta = discount.value.subtract(
            discountPerProduct.multiply(totalItems)
        ).setScale(2, RoundingMode.HALF_EVEN)

        var totalDiscount: BigDecimal
        productDescriptionList.transformAll {
            totalDiscount = discountPerProduct.multiply(BigDecimal(it.quantity))

            if (it.priceWithoutDiscount.subtract(totalDiscount) < priceThreshold) {
                val maxDiscount = it.priceWithoutDiscount.subtract(priceThreshold)
                delta = delta.add(totalDiscount.subtract(maxDiscount))

                it.copy(
                    discount = it.discount.copy(
                        method = DiscountCalcMethod.Whole,
                        value = maxDiscount,
                    )
                )
            } else {
                it.copy(
                    discount = it.discount.copy(
                        method = DiscountCalcMethod.Whole,
                        value = totalDiscount,
                    )
                )
            }
        }

        val iterator = productDescriptionList
            .sortedByQuantity()
            .listIterator()
        while (iterator.hasNext() && delta != BigDecimal.ZERO) {
            iterator.next().let { productDescription ->
                val maxDiscount = productDescription
                    .priceWithoutDiscount
                    .subtract(productDescription.discount.value)
                    .subtract(priceThreshold)

                if (maxDiscount == BigDecimal.ZERO) {
                    return@let
                }

                val actualDiscount = if (delta > maxDiscount) {
                    maxDiscount
                } else {
                    delta
                }
                delta = delta.subtract(actualDiscount)

                productDescriptionList.transformItem({
                    it.description == productDescription.description
                }) {
                    it.copy(
                        discount = it.discount.copy(
                            method = DiscountCalcMethod.Whole,
                            value = it.discount.value.add(actualDiscount),
                        )
                    )
                }
            }
        }
    }

    fun applyFeeOverall(fee: PaymentFeeDocument) {
        when (fee.method) {
            is PaymentFeeCalcMethod.None -> {
                productDescriptionList.transformAll {
                    it.copy(fee = fee.copy(value = BigDecimal.ZERO))
                }
            }

            is PaymentFeeCalcMethod.Percentage -> {
                productDescriptionList.transformAll {
                    it.copy(fee = fee.copy(value = BigDecimal.ZERO))
                }

                val priceWithDiscount = finalPrice()
                val feeAsWhole = fee.toWholeFormat(priceWithDiscount)

                applyFeeAsWhole(feeAsWhole)
            }

            is PaymentFeeCalcMethod.Whole -> {
                applyFeeAsWhole(fee)
            }
        }
    }

    private fun applyFeeAsWhole(feeAsWhole: PaymentFeeDocument) {
        val totalItems = productDescriptionList
            .map { it.quantity }
            .ifEmpty { listOf(0) }
            .reduce(Int::plus)
            .toBigDecimal()

        val feePerProduct = feeAsWhole.value
            .divide(totalItems, RoundingMode.HALF_EVEN)
            .setScale(2, RoundingMode.HALF_EVEN)
        val delta = feeAsWhole.value.subtract(
            feePerProduct.multiply(totalItems)
        )

        productDescriptionList.transformAll {
            it.copy(
                fee = it.fee.copy(
                    method = PaymentFeeCalcMethod.Whole,
                    value = feePerProduct.multiply(BigDecimal(it.quantity))
                        .setScale(2, RoundingMode.HALF_EVEN),
                ),
            )
        }

        val iterator = productDescriptionList
            .sortedByQuantity()
            .listIterator()
        if (iterator.hasNext() && delta != BigDecimal.ZERO) {
            iterator.next().let {
                productDescriptionList.transformItem({ d ->
                    it.description == d.description
                }) {
                    it.copy(
                        fee = it.fee.copy(
                            method = PaymentFeeCalcMethod.Whole,
                            value = it.fee.value.add(delta),
                        ),
                    )
                }
            }
        }
    }

    private fun productUnitDescription(
        context: Context,

        productCode: String,
        description: String,

        quantity: Int,
        discount: BigDecimal,
        fee: BigDecimal,

        priceWithoutDiscount: BigDecimal,
        priceWithFee: BigDecimal,
    ): String {
        val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

        val percentFormat = NumberFormat.getPercentInstance(currentLocale)
        percentFormat.minimumFractionDigits = 2
        percentFormat.maximumFractionDigits = 2

        val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
        currencyFormat.minimumFractionDigits = 2
        currencyFormat.maximumFractionDigits = 2

        val integerFormat = NumberFormat.getIntegerInstance(currentLocale)
        integerFormat.minimumFractionDigits = 0
        integerFormat.maximumFractionDigits = 0

        val count = integerFormat.format(quantity)

        val originalPrice = currencyFormat.format(priceWithoutDiscount)
        val finalPrice = currencyFormat.format(priceWithFee)
        val discountStr = currencyFormat.format(discount)
        val feeStr = currencyFormat.format(fee)

        val productDescriptionUnit = "<tr class=\"row16\"> <td class=\"column0 style1 s\" colspan=\"1\">${productCode.ifBlank { "-" }}</td><td class=\"column2 style1 s\">$count</td><td class=\"column3 style1 s style61\" colspan=\"5\"> ${description.ifBlank { "-" }} </td><td class=\"column8 style1 n\">$originalPrice</td><td class=\"column9 style1 n\">$discountStr</td><td class=\"column9 style1 n\">$feeStr</td><td class=\"column10 style1 n\">$finalPrice</td></tr>"

        return productDescriptionUnit
    }
}