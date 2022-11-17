package com.peyess.salesapp.features.pdf.service_order

import android.content.Context
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import com.peyess.salesapp.features.pdf.service_order.html.buildConditionsSection
import com.peyess.salesapp.features.pdf.service_order.html.buildDeliveryDateSection
import com.peyess.salesapp.features.pdf.service_order.html.buildOwnFramesSection
import com.peyess.salesapp.features.pdf.service_order.html.buildPaymentSection
import com.peyess.salesapp.features.pdf.service_order.html.buildProductListDescription
import com.peyess.salesapp.features.pdf.service_order.html.buildTakeAwaySection
import com.peyess.salesapp.features.pdf.service_order.html.generateProductData
import com.peyess.salesapp.features.pdf.service_order.html.generateSaleData
import com.peyess.salesapp.features.pdf.service_order.html.generateServiceOrder
import com.peyess.salesapp.features.pdf.service_order.html.style
import com.peyess.salesapp.features.pdf.utils.printValue
import timber.log.Timber
import java.time.format.DateTimeFormatter

private fun printFramesDescription(value: String, isOwnFrames: Boolean = false): String {
    return if (isOwnFrames) {
        "ARO PRÓPRIO"
    } else {
        printValue(value)
    }
}

private fun printAdditionData(value: String, hasAddition: Boolean = false): String {
    return if (hasAddition) {
        printValue(value)
    } else {
        "------"
    }
}

private fun printPrismData(value: String, hasPrism: Boolean = false): String {
    return if (hasPrism) {
        printValue(value)
    } else {
        "------------"
    }
}

private fun printAxisData(value: String, hasAxis: Boolean = false): String {
    return if (hasAxis) {
        printValue(value)
    } else {
        "-"
    }
}

fun buildHtml(
    context: Context,
    serviceOrder: ServiceOrderDocument,
    purchase: PurchaseDocument,
): String {
    val dayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val saleDate = serviceOrder.created

    val saleDataSection = generateSaleData(
        purchaseHid = purchase.hid,

        saleDate = saleDate,

        clientName = serviceOrder.responsibleName,
        clientBirthday = dayFormatter.format(serviceOrder.responsibleBirthday),
        clientDocument = serviceOrder.responsibleDocument,
        clientRg = "-",

        clientPhone = serviceOrder.responsiblePhone,
        clientCellphone = serviceOrder.responsibleCellphone,

        clientNeighborhood = serviceOrder.responsibleNeighborhood,
        clientStreet = serviceOrder.responsibleStreet,
        clientCity = serviceOrder.responsibleCity,
        clientState = serviceOrder.responsibleState,
        clientHouseNumber = serviceOrder.responsibleHouseNumber,
        clientZipCode = serviceOrder.responsibleZipcode,
    )

    // TODO: add clientBirthday to ServiceOrder
    val productData = generateProductData(
        soHid = serviceOrder.hid,

        clientId = serviceOrder.clientUid,
        clientName = serviceOrder.clientName,
        clientBirthday = dayFormatter.format(serviceOrder.clientBirthday),

        lensLeft = serviceOrder.leftProducts.lenses.nameDisplay.ifBlank { "-" },
        lensRight = serviceOrder.rightProducts.lenses.nameDisplay.ifBlank { "-" },
        coloringLeft = serviceOrder.leftProducts.colorings.nameDisplay.ifBlank { "-" },
        coloringRight = serviceOrder.rightProducts.colorings.nameDisplay.ifBlank { "-" },
        treatmentLeft = serviceOrder.leftProducts.treatments.nameDisplay.ifBlank { "-" },
        treatmentRight = serviceOrder.rightProducts.treatments.nameDisplay.ifBlank { "-" },

        prescriptionDate = serviceOrder.prescriptionDate.format(dayFormatter),
        prescriptionProfessional = if (serviceOrder.isCopy) {
            "CÓPIA LENSÔMETRO"
        } else {
            serviceOrder.professionalName.ifBlank { "-" }
       },

        prescriptionLeftSpherical = "%.2f".format(serviceOrder.lSpheric),
        prescriptionLeftCylindrical = "%.2f".format(serviceOrder.lCylinder),
        prescriptionLeftAxis = printAxisData("%.0f".format(serviceOrder.lAxisDegree), serviceOrder.hasAxisLeft),
        prescriptionLeftAddition = printAdditionData( "%.2f".format(serviceOrder.lAddition), serviceOrder.hasAddition),
        prescriptionLeftPrismPosition = printPrismData(serviceOrder.lPrismPos, serviceOrder.hasPrism),
        prescriptionLeftPrismDegree = printPrismData("%.2f".format(serviceOrder.lPrismDegree), serviceOrder.hasPrism),
        prescriptionLeftDnp = "%.2f".format(serviceOrder.lIpd),
        prescriptionLeftHeight = "%.2f".format(serviceOrder.lHe),

        prescriptionRightSpherical = "%.2f".format(serviceOrder.rSpheric),
        prescriptionRightCylindrical = "%.2f".format(serviceOrder.rCylinder),
        prescriptionRightAxis = printAxisData("%.0f".format(serviceOrder.rAxisDegree), serviceOrder.hasAxisRight),
        prescriptionRightAddition = printAdditionData("%.2f".format(serviceOrder.rAddition), serviceOrder.hasAddition),
        prescriptionRightPrismPosition = printPrismData(serviceOrder.rPrismPos, serviceOrder.hasPrism),
        prescriptionRightPrismDegree = printPrismData("%.2f".format(serviceOrder.rPrismDegree), serviceOrder.hasPrism),
        prescriptionRightDnp = "%.2f".format(serviceOrder.rIpd),
        prescriptionRightHeight = "%.2f".format(serviceOrder.rHe),

        framesDesign = printFramesDescription(
            serviceOrder.framesProducts.design,
            serviceOrder.hasOwnFrames,
        ),
        framesReference = printFramesDescription(
            serviceOrder.framesProducts.reference,
            serviceOrder.hasOwnFrames,
        ),
        framesColor = printFramesDescription(
            serviceOrder.framesProducts.color,
            serviceOrder.hasOwnFrames,
        ),
        framesStyle = printFramesDescription(
            serviceOrder.framesProducts.style,
            serviceOrder.hasOwnFrames,
        ),
        framesType = printFramesDescription(
            serviceOrder.framesProducts.type.toName(),
            serviceOrder.hasOwnFrames,
        ),

        measuringBridge = "%.2f".format(serviceOrder.lBridge),
        measuringVHoop = "%.2f".format(serviceOrder.lVerticalHoop),
        measuringHHoop = "%.2f".format(serviceOrder.lHorizontalHoop),

        trackingCode = "",
        trackingPortalLink = "",
    )

    val eyeSets = mutableListOf<ProductSoldEyeSetDocument>()
    eyeSets.add(serviceOrder.leftProducts)
    eyeSets.add(serviceOrder.leftProducts)

    val framesSet = mutableListOf<ProductSoldFramesDescriptionDocument>()
    if (!serviceOrder.hasOwnFrames) {
        framesSet.add(serviceOrder.framesProducts)
    }

    val miscSet = mutableListOf<ProductSoldDescriptionDocument>()
    miscSet.addAll(serviceOrder.miscProducts)

    val productListSection = buildProductListDescription(
        context = context,

        eyeSets = eyeSets,
        frames = framesSet,
        misc = miscSet,

        salesPersonName = serviceOrder.salespersonName,
        priceWithoutDiscount = serviceOrder.total,
        discount = serviceOrder.totalDiscount,
    )

    val paymentSection = buildPaymentSection(
        context = context,
        purchase = purchase,
        serviceOrder = serviceOrder,
    )

    val deliverySection = buildDeliveryDateSection()

    val takeAwayAuthorizationSection = if (serviceOrder.hasTakeaway) {
        buildTakeAwaySection(
            name = serviceOrder.takeawayName,
            document = serviceOrder.takeawayDocument,
        )
    } else {
        ""
    }

    val ownFramesSection = if (serviceOrder.hasOwnFrames) {
        buildOwnFramesSection()
    } else {
        ""
    }

    val conditions = buildConditionsSection(serviceOrder)

    val content = saleDataSection +
            productData +
            productListSection +
            paymentSection +
            deliverySection +
            takeAwayAuthorizationSection +
            ownFramesSection +
            conditions

    val htmlDocument = generateServiceOrder(style = style, content = content)
    Timber.v("Generated html")
    Timber.v(htmlDocument)

    return htmlDocument
}