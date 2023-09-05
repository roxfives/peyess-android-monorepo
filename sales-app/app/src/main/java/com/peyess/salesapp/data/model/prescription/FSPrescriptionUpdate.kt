package com.peyess.salesapp.data.model.prescription

import com.google.firebase.Timestamp

data class FSPrescriptionUpdate(
    val emitted: Timestamp = Timestamp.now(),

    val typeId: String = "",
    val typeDesc: String = "",

    val isCopy: Boolean = false,

    val patientUid: String = "",
    val patientDocument: String = "",
    val patientName: String = "",

    val professionalDocument: String = "",
    val professionalName: String = "",

    val lensTypeCategoryId: String = "",
    val lensTypeCategory: String = "",

    val hasPrism: Boolean = false,

    val hasAddition: Boolean = false,

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
    val lPrismPos: String = "",

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
    val rPrismPos: String = "",

    val observation: String = "",

    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
