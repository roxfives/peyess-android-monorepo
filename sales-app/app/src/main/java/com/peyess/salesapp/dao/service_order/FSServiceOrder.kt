package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Keep
data class FSServiceOrder(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("hid")
    val hid: String = "", // Human-friendly id (has to be unique in each store),
    @Keep
    @JvmField
    @PropertyName("director_uid")
    val directorUid: String = "",
    @Keep
    @JvmField
    @PropertyName("store_id")
    val storeId: String = "",

    @Keep
    @JvmField
    @PropertyName("picture")
    val picture: String = "",

    @Keep
    @JvmField
    @PropertyName("salesperson_uid")
    val salespersonUid: String = "",

    @Keep
    @JvmField
    @PropertyName("client_contact_type")
    val clientContactType: String = "",
    @Keep
    @JvmField
    @PropertyName("client_contact_info")
    val clientContactInfo: String = "",
    @Keep
    @JvmField
    @PropertyName("client_document")
    val clientDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("client_name")
    val clientName: String = "",
    @Keep
    @JvmField
    @PropertyName("client_picture")
    val clientPicture: String = "",
    @Keep
    @JvmField
    @PropertyName("client_uid")
    val clientUid: String = "",

    @Keep
    @JvmField
    @PropertyName("responsible_contact_type")
    val responsibleContactType: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_contact_info")
    val responsibleContactInfo: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_document")
    val responsibleDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_name")
    val responsibleName: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_picture")
    val responsiblePicture: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_uid")
    val responsibleUid: String = "",

    @Keep
    @JvmField
    @PropertyName("has_witness")
    val hasWitness: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("witness_contact_type")
    val witnessContactType: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_contact_info")
    val witnessContactInfo: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_document")
    val witnessDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_name")
    val witnessName: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_picture")
    val witnessPicture: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_uid")
    val witnessUid: String = "",

    // Lifecycle data
    @Keep
    @JvmField
    @PropertyName("state")
    val state: String = "",

    @Keep
    @JvmField
    @PropertyName("rectified")
    val rectified: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("are_there_issues")
    val areThereIssues: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("issue_description")
    val issueDescription: String = "",

    // Sales data
    @Keep
    @JvmField
    @PropertyName("same_purchase_so")
    val samePurchaseSo: List<String> = emptyList(),

    // Payment data
    @Keep
    @JvmField
    @PropertyName("payer_uids")
    val payerUids: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("products")
    val products: FSProductsSold = FSProductsSold(),

    // Denormilized prescription data
    @Keep
    @JvmField
    @PropertyName("prescription_id")
    val prescriptionId: String = "",
    @Keep
    @JvmField
    @PropertyName("l_cylinder")
    val lCylinder: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_spheric")
    val lSpheric: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_axis_degree")
    val lAxisDegree: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_addition")
    val lAddition: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_prism_axis")
    val lPrismAxis: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_prism_degree")
    val lPrismDegree: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_prism_pos")
    val lPrismPos: String = "",


    @Keep
    @JvmField
    @PropertyName("r_cylinder")
    val rCylinder: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_spheric")
    val rSpheric: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_axis_degree")
    val rAxisDegree: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_addition")
    val rAddition: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_prism_axis")
    val rPrismAxis: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_prism_degree")
    val rPrismDegree: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_prism_pos")
    val rPrismPos: String = "",

    // Denormilized measuring data
    @Keep
    @JvmField
    @PropertyName("l_ipd")
    val lIpd: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("l_measuring_id")
    val lMeasuringId: String = "",
    @Keep
    @JvmField
    @PropertyName("r_measuring_id")
    val rMeasuringId: String = "",

    @Keep
    @JvmField
    @PropertyName("l_bridge")
    val lBridge: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_diameter")
    val lDiameter: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_he")
    val lHe: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_horizontal_bridge_hoop")
    val lHorizontalBridgeHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_horizontal_hoop")
    val lHorizontalHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("l_vertical_hoop")
    val lVerticalHoop: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("r_ipd")
    val rIpd: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("r_bridge")
    val rBridge: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_diameter")
    val rDiameter: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_he")
    val rHe: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_horizontal_bridge_hoop")
    val rHorizontalBridgeHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_horizontal_hoop")
    val rHorizontalHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("r_vertical_hoop")
    val rVerticalHoop: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("sold_by")
    val soldBy: String = "",
    @Keep
    @JvmField
    @PropertyName("measure_confirmed_by")
    val measureConfirmedBy: String = "",
    @Keep
    @JvmField
    @PropertyName("discount_allowed_by")
    val discountAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("total")
    val total:  Double = 0.0,
)

fun FSServiceOrder.toDocument(): ServiceOrderDocument {
    return ServiceOrderDocument(
        id = id,

        hid = hid,
        directorUid = directorUid,
        storeId = storeId,

        picture = picture,

        salespersonUid = salespersonUid,

        clientContactType = clientContactType,
        clientContactInfo = clientContactInfo,
        clientDocument = clientDocument,
        clientName = clientName,
        clientPicture = clientPicture,
        clientUid = clientUid,

        responsibleContactType = responsibleContactType,
        responsibleContactInfo = responsibleContactInfo,
        responsibleDocument = responsibleDocument,
        responsibleName = responsibleName,
        responsiblePicture = responsiblePicture,
        responsibleUid = responsibleUid,

        hasWitness = hasWitness,
        witnessContactType = witnessContactType,
        witnessContactInfo = witnessContactInfo,
        witnessDocument = witnessDocument,
        witnessName = witnessName,
        witnessPicture = witnessPicture,
        witnessUid = witnessUid,

        // Lifecycle data
        state = state,

        rectified = rectified,

        areThereIssues = areThereIssues,
        issueDescription = issueDescription,

        // Sales data
        samePurchaseSo = samePurchaseSo,

        // Payment data
        payerUids = payerUids,

        products = products.toDocument(),

        // Denormilized prescription data
        prescriptionId = prescriptionId,
        lCylinder = lCylinder,
        lSpheric = lSpheric,
        lAxisDegree = lAxisDegree,
        lAddition = lAddition,
        lPrismAxis = lPrismAxis,
        lPrismDegree = lPrismDegree,
        lPrismPos = lPrismPos,


        rCylinder = rCylinder,
        rSpheric = rSpheric,
        rAxisDegree = rAxisDegree,
        rAddition = rAddition,
        rPrismAxis = rPrismAxis,
        rPrismDegree = rPrismDegree,
        rPrismPos = rPrismPos,

        // Denormilized measuring data
        lIpd = lIpd,

        lMeasuringId = lMeasuringId,
        rMeasuringId = rMeasuringId,

        lBridge = lBridge,
        lDiameter = lDiameter,
        lHe = lHe,
        lHorizontalBridgeHoop = lHorizontalBridgeHoop,
        lHorizontalHoop = lHorizontalHoop,
        lVerticalHoop = lVerticalHoop,

        rIpd = rIpd,

        rBridge = rBridge,
        rDiameter = rDiameter,
        rHe = rHe,
        rHorizontalBridgeHoop = rHorizontalBridgeHoop,
        rHorizontalHoop = rHorizontalHoop,
        rVerticalHoop = rVerticalHoop,

        soldBy = soldBy,
        measureConfirmedBy = measureConfirmedBy,
        discountAllowedBy = discountAllowedBy,

        created = created.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,

        updated = updated.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,

        total = total,
    )
}
