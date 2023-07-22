package com.peyess.salesapp.data.model.prescription

import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.prescription.PrismPosition
import java.time.ZonedDateTime

data class PrescriptionDocument(
    val id: String = "",

    val storeId: String = "",
    val storeIds: List<String> = emptyList(),

    val emitted: ZonedDateTime = ZonedDateTime.now(),

    val typeId: String = "",
    val typeDesc: String = "",

    val isCopy: Boolean = false,

    val patientDocument: String = "",
    val patientName: String = "",

    val patientUid: String = "",
    val professionalDocument: String = "",
    val professionalName: String = "",

    val lensTypeCategoryId: String = "",
    val lensTypeCategory: LensTypeCategoryName = LensTypeCategoryName.None,

    val hasPrism: Boolean = false,

    val hasAddition: Boolean = false,

    val pd: Double = 0.0,

    val lIpd: Double = 0.0,
    val lBridge: Double = 0.0,
    val lBridgeHoop: Double = 0.0,
    val lHHoop: Double = 0.0,
    val lHe: Double = 0.0,
    val lVHoop: Double = 0.0,
    val lDiameter: Double = 0.0,

    val lCylinder: Double = 0.0,
    val lSpherical: Double = 0.0,
    val lAxisDegree: Double = 0.0,
    val lAddition: Double = 0.0,
    val lPrismAxis: Double = 0.0,
    val lPrismDegree: Double = 0.0,
    val lPrismPos: PrismPosition = PrismPosition.None,

    val rIpd: Double = 0.0,
    val rBridge: Double = 0.0,
    val rBridgeHoop: Double = 0.0,
    val rHHoop: Double = 0.0,
    val rHe: Double = 0.0,
    val rVHoop: Double = 0.0,
    val rDiameter: Double = 0.0,

    val rCylinder: Double = 0.0,
    val rSpherical: Double = 0.0,
    val rAxisDegree: Double = 0.0,
    val rAddition: Double = 0.0,
    val rPrismAxis: Double = 0.0,
    val rPrismDegree: Double = 0.0,
    val rPrismPos: PrismPosition = PrismPosition.None,

    val observation: String = "",

    val docVersion: Int = 0,
    val isEditable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
