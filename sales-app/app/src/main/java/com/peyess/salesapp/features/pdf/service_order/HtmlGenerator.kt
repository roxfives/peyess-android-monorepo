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
import java.time.format.DateTimeFormatter

fun buildHtml(
    context: Context,
    serviceOrder: ServiceOrderDocument,
    purchase: PurchaseDocument,
): String {
    val dayFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val hourFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val saleDate = serviceOrder.created

    val saleDataSection = generateSaleData(
        saleHid = serviceOrder.hid,

        saleDate = saleDate.format(dayFormatter),
        saleHour = saleDate.format(hourFormatter),

        clientName = serviceOrder.responsibleName,
        clientBirthday = dayFormatter.format(serviceOrder.responsibleBirthday),
        clientId = serviceOrder.responsibleUid,
        clientDocument = serviceOrder.responsibleDocument,
        clientRg = "-",

        clientPhone = serviceOrder.responsiblePhone,
        clientCellphone = serviceOrder.responsibleCellphone,

        clientNeighborhood = serviceOrder.responsibleNeighborhood,
        clientStreet = serviceOrder.responsibleStreet,
        clientCity = serviceOrder.responsibleCity,
        clientState = serviceOrder.responsibleState,
        clientHouseNumber = serviceOrder.responsibleHouseNumber,
        clientZipCode = serviceOrder.responsibleZipCode,
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
        prescriptionProfessional = serviceOrder.professionalName.ifBlank { "-" },

        prescriptionLeftSpherical = "%.2f".format(serviceOrder.lSpheric),
        prescriptionLeftCylindrical = "%.2f".format(serviceOrder.lCylinder),
        prescriptionLeftAxis = "%.2f".format(serviceOrder.lAxisDegree),
        prescriptionLeftAddition = "%.2f".format(serviceOrder.lAddition),
        prescriptionLeftPrismPosition = serviceOrder.lPrismPos,
        prescriptionLeftPrismDegree = "%.2f".format(serviceOrder.lPrismDegree),
        prescriptionLeftDnp = "%.2f".format(serviceOrder.lIpd),
        prescriptionLeftHeight = "%.2f".format(serviceOrder.lHe),

        prescriptionRightSpherical = "%.2f".format(serviceOrder.rSpheric),
        prescriptionRightCylindrical = "%.2f".format(serviceOrder.rCylinder),
        prescriptionRightAxis = "%.2f".format(serviceOrder.rAxisDegree),
        prescriptionRightAddition = "%.2f".format(serviceOrder.rAddition),
        prescriptionRightPrismPosition = serviceOrder.rPrismPos,
        prescriptionRightPrismDegree = "%.2f".format(serviceOrder.rPrismDegree),
        prescriptionRightDnp = "%.2f".format(serviceOrder.rIpd),
        prescriptionRightHeight = "%.2f".format(serviceOrder.rHe),

        framesDesign = serviceOrder.framesProducts.design,
        framesReference = serviceOrder.framesProducts.reference,
        framesColor = serviceOrder.framesProducts.color,
        framesStyle = serviceOrder.framesProducts.style,
        framesType = serviceOrder.framesProducts.type.toName(),

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
            takeAwayAuthorizationSection +
            ownFramesSection +
            deliverySection +
            conditions

    return generateServiceOrder(style = style, content = content)
}