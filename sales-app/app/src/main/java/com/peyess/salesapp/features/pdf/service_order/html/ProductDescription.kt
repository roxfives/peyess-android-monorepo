package com.peyess.salesapp.features.pdf.service_order.html

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

// Unit
// <!-- cod_loja, cod_geral, qtd, produto, valor, desconto, total -->

// Total
//<!-- vendedor, valor_sem_desconto, desconto, total -->

private val productDescriptionHeader = "<tr class=\"row14\"> <td class=\"column0 style62 s style62\" colspan=\"11\"> DESCRIÇÃO DOS PRODUTOS </td></tr><tr class=\"row15\"> <td class=\"column0 style8 s\" colspan=\"1\">Código</td><td class=\"column2 style8 s\">Qt.</td><td class=\"column3 style60 s style60\" colspan=\"5\"> Produto </td><td class=\"column8 style8 s\">Valor</td><td class=\"column9 style8 s\">Desconto</td><td class=\"column9 style8 s\">Taxa</td><td class=\"column10 style8 s\">R\$</td></tr>"

private fun productUnitDescription(
    context: Context,

    productCode: String,
    description: String,

    quantity: Int,
    priceWithoutDiscount: Double,
    discount: Double,
    fee: Double,
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
    val priceWithDiscount = priceWithoutDiscount * (1 - discount)
    val priceWithFee = priceWithDiscount * (1 + fee)

    val originalPrice = currencyFormat.format(priceWithoutDiscount)
    val finalPrice = currencyFormat.format(priceWithFee)
    val discountStr = percentFormat.format(discount)
    val feeStr = percentFormat.format(fee)

    val productDescriptionUnit = "<tr class=\"row16\"> <td class=\"column0 style1 s\" colspan=\"1\">${productCode.ifBlank { "-" }}</td><td class=\"column2 style1 s\">$count</td><td class=\"column3 style1 s style61\" colspan=\"5\"> ${description.ifBlank { "-" }} </td><td class=\"column8 style1 n\">$originalPrice</td><td class=\"column9 style1 n\">$discountStr</td><td class=\"column9 style1 n\">$feeStr</td><td class=\"column10 style1 n\">$finalPrice</td></tr>"

    return productDescriptionUnit
}

private fun productDescriptionFooter(
    context: Context,
    salesPersonName: String,
    priceWithoutDiscount: Double,
    discount: Double,
    fee: Double,
): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val percentFormat = NumberFormat.getPercentInstance(currentLocale)
    percentFormat.minimumFractionDigits = 2
    percentFormat.maximumFractionDigits = 2

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val priceWithDiscount = priceWithoutDiscount * (1 - discount)
    val priceWithFee = priceWithDiscount * (1 + fee)

    val priceWithoutDiscountStr = currencyFormat.format(priceWithoutDiscount)
    val priceWithFeeStr = currencyFormat.format(priceWithFee)
    val discountStr = percentFormat.format(discount)
    val feeStr = percentFormat.format(fee)

    val productDescriptionTotal = "<tr class=\"row22\"> <td class=\"column0 styleProductListFooterName s\" colspan=\"7\"> Vendedor: ${salesPersonName.ifBlank { "-" }} </td><td class=\"column8 styleProductListFooter n\">$priceWithoutDiscountStr</td><td class=\"column9 styleProductListFooter n\">$discountStr</td><td class=\"column9 styleProductListFooter n\">$feeStr</td><td class=\"column10 styleProductListFooter n\">$priceWithFeeStr</td></tr>"

    return productDescriptionTotal
}

private fun discountAsPercentage(discount: DiscountDescriptionDocument, totalPrice: Double): Double {
    return when (discount.method) {
        DiscountCalcMethod.None ->
            0.0
        DiscountCalcMethod.Percentage ->
            discount.value.toDouble()
        DiscountCalcMethod.Whole ->
            discount.value
                .divide(BigDecimal(totalPrice), RoundingMode.HALF_EVEN)
                .toDouble()
    }
}

private fun feeAsPercentage(fee: PaymentFeeDocument, totalPrice: Double): Double {
    return when (fee.method) {
        PaymentFeeCalcMethod.None ->
            0.0
        PaymentFeeCalcMethod.Percentage ->
            fee.value
        PaymentFeeCalcMethod.Whole ->
            fee.value / totalPrice
    }
}

private fun buildProductList(
    context: Context,

    eyeSets: List<ProductSoldEyeSetDocument>,
    frames: List<ProductSoldFramesDescriptionDocument>,
    misc: List<ProductSoldDescriptionDocument>,

    isGeneralDiscount: Boolean = false,
    generalDiscount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    generalFee: PaymentFeeDocument = PaymentFeeDocument(),
): String {
    val sets: MutableList<String> = mutableListOf()

    var lensDiscount: DiscountDescriptionDocument
    var coloringsDiscount: DiscountDescriptionDocument
    var treatmentsDiscount: DiscountDescriptionDocument
    var framesDiscount: DiscountDescriptionDocument
    var miscDiscount: DiscountDescriptionDocument

    var lensFee: PaymentFeeDocument
    var coloringsFee: PaymentFeeDocument
    var treatmentsFee: PaymentFeeDocument
    var framesFee: PaymentFeeDocument
    var miscFee: PaymentFeeDocument

    eyeSets.forEach { set ->
        lensDiscount = if (isGeneralDiscount) {
            generalDiscount
        } else {
            set.lenses.discount
        }
        lensFee = if (isGeneralDiscount) {
            generalFee
        } else {
            PaymentFeeDocument()
        }


        coloringsDiscount = if (isGeneralDiscount) {
            generalDiscount
        } else {
            set.colorings.discount
        }
        coloringsFee = if (isGeneralDiscount) {
            generalFee
        } else {
            PaymentFeeDocument()
        }

        treatmentsDiscount = if (isGeneralDiscount) {
            generalDiscount
        } else {
            set.treatments.discount
        }
        treatmentsFee = if (isGeneralDiscount) {
            generalFee
        } else {
            PaymentFeeDocument()
        }

        sets.add(
            productUnitDescription(
                context = context,
                productCode = "",
                quantity = set.lenses.units,
                priceWithoutDiscount = set.lenses.price,
                discount = discountAsPercentage(lensDiscount, set.lenses.price),
                description = set.lenses.nameDisplay,
                fee = feeAsPercentage(
                    lensFee,
                    set.lenses.price * (1 - discountAsPercentage(lensDiscount, set.lenses.price)),
                ),
            ),
        )

        // TODO: Refactor this set to have the data about being a placeholder only
        if (
            set.colorings.nameDisplay.trim() != "Indisponível"
            && set.colorings.nameDisplay.trim() != "Incolor"
        ) {
            sets.add(
                productUnitDescription(
                    context = context,
                    productCode = "",
                    quantity = set.colorings.units,
                    priceWithoutDiscount = set.colorings.price,
                    discount = discountAsPercentage(coloringsDiscount, set.colorings.price),
                    description = set.colorings.nameDisplay,
                    fee = feeAsPercentage(
                        coloringsFee,
                        set.colorings.price * (1 - discountAsPercentage(coloringsDiscount, set.colorings.price)),
                    ),
                ),
            )
        }

        if (
            set.treatments.nameDisplay.trim() != "Indisponível"
            && set.treatments.nameDisplay.trim() != "Incolor"
        ) {
            sets.add(
                productUnitDescription(
                    context = context,
                    productCode = "",
                    quantity = set.treatments.units,
                    priceWithoutDiscount = set.treatments.price,
                    discount = discountAsPercentage(treatmentsDiscount, set.treatments.price),
                    description = set.treatments.nameDisplay,
                    fee = feeAsPercentage(
                        treatmentsFee,
                        set.treatments.price * (1 - discountAsPercentage(treatmentsDiscount, set.treatments.price)),
                    ),
                ),
            )
        }
    }

    frames.forEach {
        framesDiscount = if (isGeneralDiscount) {
            generalDiscount
        } else {
            it.discount
        }
        framesFee = if (isGeneralDiscount) {
            generalFee
        } else {
            PaymentFeeDocument()
        }

        sets.add(
            productUnitDescription(
                context = context,
                productCode = "",
                quantity = it.units,
                priceWithoutDiscount = it.price,
                discount = discountAsPercentage(framesDiscount, it.price),
                description = it.nameDisplay,
                fee = feeAsPercentage(
                    framesFee,
                    it.price * (1 - discountAsPercentage(framesDiscount, it.price)),
                ),
            ),
        )
    }

    misc.forEach {
        miscDiscount = if (isGeneralDiscount) {
            generalDiscount
        } else {
            it.discount
        }
        miscFee = if (isGeneralDiscount) {
            generalFee
        } else {
            PaymentFeeDocument()
        }

        sets.add(
            productUnitDescription(
                context = context,
                productCode = "",
                quantity = it.units,
                priceWithoutDiscount = it.price,
                discount = discountAsPercentage(miscDiscount, it.price),
                description = it.nameDisplay,
                fee = feeAsPercentage(
                    miscFee,
                    it.price * (1 - discountAsPercentage(miscDiscount, it.price)),
                ),
            ),
        )
    }

    var products = ""
    sets.forEach { products += it }
    return products
}

fun buildProductListDescription(
    context: Context,

    eyeSets: List<ProductSoldEyeSetDocument>,
    frames: List<ProductSoldFramesDescriptionDocument>,
    misc: List<ProductSoldDescriptionDocument>,

    salesPersonName: String,
    priceWithoutDiscount: Double,
    discount: Double,
    fee: Double,
): String {
    val header = productDescriptionHeader

    val productList = buildProductList(
        context = context,
        eyeSets = eyeSets,
        frames = frames,
        misc = misc,

        isGeneralDiscount = true,
        generalDiscount = DiscountDescriptionDocument(
            method = DiscountCalcMethod.Percentage,
            value = BigDecimal(discount),
        ),
        generalFee = PaymentFeeDocument(
            saleId = "",
            method = PaymentFeeCalcMethod.Percentage,
            value = fee,
        ),
    )

    val footer = productDescriptionFooter(context, salesPersonName, priceWithoutDiscount, discount, fee)

    return header + productList + footer
}