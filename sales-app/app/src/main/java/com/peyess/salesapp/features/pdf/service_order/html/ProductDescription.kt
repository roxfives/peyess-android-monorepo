package com.peyess.salesapp.features.pdf.service_order.html

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.features.pdf.service_order.model.ProductDescription
import com.peyess.salesapp.features.pdf.service_order.model.ProductDescriptionList
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

private fun productDescriptionFooter(
    context: Context,
    salesPersonName: String,

    discount: BigDecimal,
    fee: BigDecimal,

    priceWithoutDiscount: BigDecimal,
    finalPrice: BigDecimal,
): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val priceWithoutDiscountStr = currencyFormat.format(priceWithoutDiscount)
    val priceWithFeeStr = currencyFormat.format(finalPrice)
    val discountStr = currencyFormat.format(discount)
    val feeStr = currencyFormat.format(fee)

    val productDescriptionTotal = "<tr class=\"row22\"> <td class=\"column0 styleProductListFooterName s\" colspan=\"7\"> Vendedor: ${salesPersonName.ifBlank { "-" }} </td><td class=\"column8 styleProductListFooter n\">$priceWithoutDiscountStr</td><td class=\"column9 styleProductListFooter n\">$discountStr</td><td class=\"column9 styleProductListFooter n\">$feeStr</td><td class=\"column10 styleProductListFooter n\">$priceWithFeeStr</td></tr>"

    return productDescriptionTotal
}

fun buildProductListDescription(
    context: Context,

    eyeSets: List<ProductSoldEyeSetDocument>,
    frames: List<ProductSoldFramesDescriptionDocument>,
    misc: List<ProductSoldDescriptionDocument>,

    salesPersonName: String,
    priceWithoutDiscount: BigDecimal,
    discount: DiscountDescriptionDocument,
    fee: PaymentFeeDocument,
): String {
    val header = productDescriptionHeader

    val productList = buildProductList(
        eyeSets = eyeSets,
        frames = frames,
        misc = misc,

        isOverallDiscount = true,
        generalDiscount = discount,
        generalFee = fee,
    )

    val totalDiscount = productList.totalWholeDiscount()
    val totalFee = productList.totalWholeFee()
    val finalPrice = productList.finalPrice()
    val footer = productDescriptionFooter(
        context = context,
        salesPersonName = salesPersonName,
        discount = totalDiscount,
        fee = totalFee,
        priceWithoutDiscount = priceWithoutDiscount,
        finalPrice = finalPrice,
    )

    return header + productList.productListDescription(context) + footer
}

private fun buildProductList(
    eyeSets: List<ProductSoldEyeSetDocument>,
    frames: List<ProductSoldFramesDescriptionDocument>,
    misc: List<ProductSoldDescriptionDocument>,

    isOverallDiscount: Boolean = true,
    generalDiscount: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    generalFee: PaymentFeeDocument = PaymentFeeDocument(),
): ProductDescriptionList {
    val productList = ProductDescriptionList()

    var lensDiscount: DiscountDescriptionDocument
    var lensAccessoryDiscount: DiscountDescriptionDocument
    var coloringsDiscount: DiscountDescriptionDocument
    var coloringAccessoryDiscount: DiscountDescriptionDocument
    var treatmentsDiscount: DiscountDescriptionDocument
    var treatmentAccessoryDiscount: DiscountDescriptionDocument
    var framesDiscount: DiscountDescriptionDocument
    var framesAccessoryDiscount: DiscountDescriptionDocument
    var miscDiscount: DiscountDescriptionDocument

    var lensFee: PaymentFeeDocument
    var lensAccessoryFee: PaymentFeeDocument
    var coloringsFee: PaymentFeeDocument
    var coloringAccessoryFee: PaymentFeeDocument
    var treatmentsFee: PaymentFeeDocument
    var treatmentAccessoryFee: PaymentFeeDocument
    var framesFee: PaymentFeeDocument
    var framesAccessoryFee: PaymentFeeDocument
    var miscFee: PaymentFeeDocument

    eyeSets.forEach { set ->
        lensDiscount = if (isOverallDiscount) {
            DiscountDescriptionDocument()
        } else {
            set.lenses.discount
        }
        lensFee = PaymentFeeDocument()

        coloringsDiscount = if (isOverallDiscount) {
            DiscountDescriptionDocument()
        } else {
            set.colorings.discount
        }
        coloringsFee = PaymentFeeDocument()

        treatmentsDiscount = if (isOverallDiscount) {
            DiscountDescriptionDocument()
        } else {
            set.treatments.discount
        }
        treatmentsFee = PaymentFeeDocument()

        productList.addProductDescription(
            ProductDescription(
                productCode = "",
                quantity = set.lenses.units,
                priceWithoutDiscount = set.lenses.price,
                discount = lensDiscount,
                description = set.lenses.nameDisplay,
                fee = lensFee,
            )
        )

        set.lenses.accessoryPerUnit.forEach {
            lensAccessoryDiscount = if (isOverallDiscount) {
                DiscountDescriptionDocument()
            } else {
                it.discount
            }
            lensAccessoryFee = PaymentFeeDocument()

            productList.addProductDescription(
                ProductDescription(
                    productCode = "",
                    quantity = set.lenses.units,
                    priceWithoutDiscount = it.price,
                    discount = lensAccessoryDiscount,
                    description = it.nameDisplay,
                    fee = lensAccessoryFee,
                )
            )
        }

        // TODO: Refactor this set to have the data about being a placeholder only
        if (
            set.colorings.nameDisplay.trim() != "Indisponível"
            && set.colorings.nameDisplay.trim() != "Incolor"
            && !set.colorings.isIncluded
        ) {
            productList.addProductDescription(
                ProductDescription(
                    productCode = "",
                    quantity = set.colorings.units,
                    priceWithoutDiscount = set.colorings.price,
                    discount = coloringsDiscount,
                    description = set.colorings.nameDisplay,
                    fee = coloringsFee,
                )
            )

            set.colorings.accessoryPerUnit.forEach {
                coloringAccessoryDiscount = if (isOverallDiscount) {
                    DiscountDescriptionDocument()
                } else {
                    it.discount
                }
                coloringAccessoryFee = PaymentFeeDocument()

                productList.addProductDescription(
                    ProductDescription(
                        productCode = "",
                        quantity = set.colorings.units,
                        priceWithoutDiscount = it.price,
                        discount = coloringAccessoryDiscount,
                        description = it.nameDisplay,
                        fee = coloringAccessoryFee,
                    )
                )
            }
        }

        if (
            set.treatments.nameDisplay.trim() != "Indisponível"
            && set.treatments.nameDisplay.trim() != "Incolor"
            && !set.treatments.isIncluded
        ) {
            productList.addProductDescription(
                ProductDescription(
                    productCode = "",
                    quantity = set.treatments.units,
                    priceWithoutDiscount = set.treatments.price,
                    discount = treatmentsDiscount,
                    description = set.treatments.nameDisplay,
                    fee = treatmentsFee,
                )
            )

            set.treatments.accessoryPerUnit.forEach {
                treatmentAccessoryDiscount = if (isOverallDiscount) {
                    DiscountDescriptionDocument()
                } else {
                    it.discount
                }
                treatmentAccessoryFee = PaymentFeeDocument()

                productList.addProductDescription(
                    ProductDescription(
                        productCode = "",
                        quantity = set.treatments.units,
                        priceWithoutDiscount = it.price,
                        discount = treatmentAccessoryDiscount,
                        description = it.nameDisplay,
                        fee = treatmentAccessoryFee,
                    )
                )
            }
        }
    }

    frames.forEach { framesItem ->
        framesDiscount = if (isOverallDiscount) {
            DiscountDescriptionDocument()
        } else {
            framesItem.discount
        }
        framesFee = PaymentFeeDocument()

        productList.addProductDescription(
            ProductDescription(
                productCode = "",
                quantity = framesItem.units,
                priceWithoutDiscount = framesItem.price,
                discount = framesDiscount,
                description = framesItem.nameDisplay,
                fee = framesFee,
            )
        )

        framesItem.accessoriesPerUnit.forEach { framesAccessory ->
            framesAccessoryDiscount = if (isOverallDiscount) {
                DiscountDescriptionDocument()
            } else {
                framesAccessory.discount
            }
            framesAccessoryFee = PaymentFeeDocument()

            productList.addProductDescription(
                ProductDescription(
                    productCode = "",
                    quantity = framesItem.units,
                    priceWithoutDiscount = framesAccessory.price,
                    discount = framesAccessoryDiscount,
                    description = framesAccessory.nameDisplay,
                    fee = framesAccessoryFee,
                )
            )
        }
    }

    misc.forEach {
        miscDiscount = if (isOverallDiscount) {
            DiscountDescriptionDocument()
        } else {
            it.discount
        }
        miscFee = PaymentFeeDocument()

        productList.addProductDescription(
            ProductDescription(
                productCode = "",
                quantity = it.units,
                priceWithoutDiscount = it.price,
                discount = miscDiscount,
                description = it.nameDisplay,
                fee = miscFee,
            )
        )
    }

    if (isOverallDiscount) {
        productList.applyDiscountOverall(generalDiscount)
    }
    productList.applyFeeOverall(generalFee)

//    var products = ""
//    sets.forEach { products += it }
//    return products
    return productList
}