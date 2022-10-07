package com.peyess.salesapp.data.model.sale.service_order

import androidx.annotation.Keep
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductsSoldDocument
import java.time.ZonedDateTime

@Keep
data class ServiceOrderDocument(
    val id: String = "",

    val hid: String = "", // Human-friendly id (has to be unique in each store),
    val storeId: String = "",
    val storeIds: List<String> = emptyList(),

    val picture: String = "",

    val salespersonUid: String = "",

    val clientDocument: String = "",
    val clientName: String = "",
    val clientPicture: String = "",
    val clientUid: String = "",

    val responsibleDocument: String = "",
    val responsibleName: String = "",
    val responsiblePicture: String = "",
    val responsibleUid: String = "",

    val hasWitness: Boolean = false,
    val witnessDocument: String = "",
    val witnessName: String = "",
    val witnessPicture: String = "",
    val witnessUid: String = "",

    // Lifecycle data
    val state: String = "",

    val rectified: Boolean = false,

    val areThereIssues: Boolean = false,
    val issueDescription: String = "",

    // Sales data
    val samePurchaseSo: List<String> = emptyList(),

    // Payment data
    val purchaseId: String = "",
    val payerUids: List<String> = emptyList(),
    val payerDocuments: List<String> = emptyList(),

    val products: ProductsSoldDocument = ProductsSoldDocument(),

    // Denormilized prescription data
    val prescriptionId: String = "",
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

    // Denormilized measuring data
    val lIpd: Double = 0.0,

    val lPositioningId: String = "",
    val rPositioningId: String = "",

    val lMeasuringId: String = "",
    val rMeasuringId: String = "",

    val lBridge: Double = 0.0,
    val lDiameter: Double = 0.0,
    val lHe: Double = 0.0,
    val lHorizontalBridgeHoop: Double = 0.0,
    val lHorizontalHoop: Double = 0.0,
    val lVerticalHoop: Double = 0.0,

    val rIpd: Double = 0.0,

    val rBridge: Double = 0.0,
    val rDiameter: Double = 0.0,
    val rHe: Double = 0.0,
    val rHorizontalBridgeHoop: Double = 0.0,
    val rHorizontalHoop: Double = 0.0,
    val rVerticalHoop: Double = 0.0,

    val soldBy: String = "",
    val measureConfirmedBy: String = "",
    val discountAllowedBy: String = "",

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy:  String = "",
    val createAllowedBy:  String = "",

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy:  String = "",
    val updateAllowedBy:  String = "",

    val total:  Double = 0.0,
)