package com.peyess.salesapp.data.model.prescription

import com.google.firebase.Timestamp
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import java.time.ZonedDateTime

data class PrescriptionDocument(
    val id: String = "",

    val emitted: ZonedDateTime = ZonedDateTime.now(),
    val picture: String = "",

    val typeId: String = "",
    val typeDesc: String = "",

    val isCopy: Boolean = false,

    val patientDocument: String = "",
    val patientName: String = "",

    val professionalDocument: String = "",
    val professionalName: String = "",

    val lensType: LensTypeCategoryName = LensTypeCategoryName.Multi,
    val hasPrism: Boolean = false,

    val hasAddition: Boolean = false,

    val pd: Double = 0.0,

    val lIpd: Double = 0.0,
    val lCylinder: Double = 0.0,
    val lSpherical: Double = 0.0,
    val lAxisDegree: Double = 0.0,
    val lAddition: Double = 0.0,
    val lPrismAxis: Double = 0.0,
    val lPrismDegree: Double = 0.0,
    val lPrismPos: PrismPosition = PrismPosition.None,

    val rIpd: Double = 0.0,
    val rCylinder: Double = 0.0,
    val rSpherical: Double = 0.0,
    val rAxisDegree: Double = 0.0,
    val rAddition: Double = 0.0,
    val rPrismAxis: Double = 0.0,
    val rPrismDegree: Double = 0.0,
    val rPrismPos: PrismPosition = PrismPosition.None,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
