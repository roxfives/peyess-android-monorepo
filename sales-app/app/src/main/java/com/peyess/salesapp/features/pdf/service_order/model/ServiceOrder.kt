package com.peyess.salesapp.features.pdf.service_order.model

import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument
import java.math.BigDecimal
import java.time.ZonedDateTime

data class ServiceOrder(
    val hid: String = "",

    val clientUid: String = "",
    val clientDocument: String = "",
    val clientName: String = "",
    val clientBirthday: ZonedDateTime = ZonedDateTime.now(),
    val clientPhone: String = "",
    val clientCellphone: String = "",
    val clientNeighborhood: String = "",
    val clientStreet: String = "",
    val clientCity: String = "",
    val clientState: String = "",
    val clientHouseNumber: String = "",
    val clientZipcode: String = "",

    val responsibleUid: String = "",
    val responsibleDocument: String = "",
    val responsibleName: String = "",
    val responsibleBirthday: ZonedDateTime = ZonedDateTime.now(),
    val responsiblePhone: String = "",
    val responsibleCellphone: String = "",
    val responsibleNeighborhood: String = "",
    val responsibleStreet: String = "",
    val responsibleCity: String = "",
    val responsibleState: String = "",
    val responsibleHouseNumber: String = "",
    val responsibleZipcode: String = "",

    val witnessUid: String = "",
    val hasWitness: Boolean = false,
    val witnessDocument: String = "",
    val witnessName: String = "",
    val witnessBirthday: ZonedDateTime = ZonedDateTime.now(),
    val witnessPhone: String = "",
    val witnessCellphone: String = "",
    val witnessNeighborhood: String = "",
    val witnessStreet: String = "",
    val witnessCity: String = "",
    val witnessState: String = "",
    val witnessHouseNumber: String = "",
    val witnessZipcode: String = "",

    val hasTakeaway: Boolean = false,
    val takeawayName: String = "",
    val takeawayDocument: String = "",

    val samePurchaseSo: List<String> = emptyList(),

    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),

    val hasOwnFrames: Boolean = false,
    val leftProducts: ProductSoldEyeSetDocument = ProductSoldEyeSetDocument(),
    val rightProducts: ProductSoldEyeSetDocument = ProductSoldEyeSetDocument(),
    val framesProducts: ProductSoldFramesDescriptionDocument = ProductSoldFramesDescriptionDocument(),
    val miscProducts: List<ProductSoldDescriptionDocument> = emptyList(),

    val isCopy: Boolean = false,
    val professionalName: String = "",
    val professionalId: String = "",
    val prescriptionDate: ZonedDateTime = ZonedDateTime.now(),
    val hasAddition: Boolean = false,
    val hasPrism: Boolean = false,
    val lCylinder: Double = 0.0,
    val lSpheric: Double = 0.0,
    val lAxisDegree: Double = 0.0,
    val lAddition: Double = 0.0,
    val lPrismAxis: Double = 0.0,
    val lPrismDegree: Double = 0.0,
    val lPrismPos: String = "",
    val rCylinder: Double = 0.0,
    val rSpheric: Double = 0.0,
    val rAxisDegree: Double = 0.0,
    val rAddition: Double = 0.0,
    val rPrismAxis: Double = 0.0,
    val rPrismDegree: Double = 0.0,
    val rPrismPos: String = "",

    val lIpd: Double = 0.0,
    val lBridge: Double = 0.0,
    val lBridgeHoop: Double = 0.0,
    val lDiameter: Double = 0.0,
    val lHe: Double = 0.0,
    val lHorizontalBridgeHoop: Double = 0.0,
    val lHorizontalHoop: Double = 0.0,
    val lVerticalHoop: Double = 0.0,
    val rIpd: Double = 0.0,
    val rBridge: Double = 0.0,
    val rBridgeHoop: Double = 0.0,
    val rDiameter: Double = 0.0,
    val rHe: Double = 0.0,
    val rHorizontalBridgeHoop: Double = 0.0,
    val rHorizontalHoop: Double = 0.0,
    val rVerticalHoop: Double = 0.0,

    val measureConfirmedBy: String = "",
    val discountAllowedBy: String = "",

    val fullPrice:  BigDecimal = BigDecimal.ZERO,
    val finalPrice:  BigDecimal = BigDecimal.ZERO,

    val observation:  String = "",

    val created: ZonedDateTime = ZonedDateTime.now(),

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy:  String = "",
    val updateAllowedBy:  String = "",
) {
    val hasAxisLeft = lCylinder < 0.0
    val hasAxisRight = rCylinder < 0.0
}
