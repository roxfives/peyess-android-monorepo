package com.peyess.salesapp.data.model.sale.service_order

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductSoldEyeSet
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductsSold
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductsSoldDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldFramesDescription
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldFramesDescriptionDocument

@Keep
@IgnoreExtraProperties
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
    @PropertyName("store_id")
    val storeId: String = "",

    @Keep
    @JvmField
    @PropertyName("store_ids")
    val storeIds: List<String> = emptyList(),

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
    @PropertyName("client_uid")
    val clientUid: String = "",
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
    @PropertyName("client_birthday")
    val clientBirthday: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("client_phone")
    val clientPhone: String = "",
    @Keep
    @JvmField
    @PropertyName("client_cellphone")
    val clientCellphone: String = "",
    @Keep
    @JvmField
    @PropertyName("client_neighborhood")
    val clientNeighborhood: String = "",
    @Keep
    @JvmField
    @PropertyName("client_street")
    val clientStreet: String = "",
    @Keep
    @JvmField
    @PropertyName("client_city")
    val clientCity: String = "",
    @Keep
    @JvmField
    @PropertyName("client_state")
    val clientState: String = "",
    @Keep
    @JvmField
    @PropertyName("client_housenumber")
    val clientHouseNumber: String = "",
    @Keep
    @JvmField
    @PropertyName("client_zipcode")
    val clientZipcode: String = "",

    @Keep
    @JvmField
    @PropertyName("responsible_uid")
    val responsibleUid: String = "",
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
    @PropertyName("responsible_birthday")
    val responsibleBirthday: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("responsible_phone")
    val responsiblePhone: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_cellphone")
    val responsibleCellphone: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_neighborhood")
    val responsibleNeighborhood: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_street")
    val responsibleStreet: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_city")
    val responsibleCity: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_state")
    val responsibleState: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_housenumber")
    val responsibleHouseNumber: String = "",
    @Keep
    @JvmField
    @PropertyName("responsible_zipcode")
    val responsibleZipcode: String = "",


    @Keep
    @JvmField
    @PropertyName("witness_uid")
    val witnessUid: String = "",
    @Keep
    @JvmField
    @PropertyName("has_witness")
    val hasWitness: Boolean = false,
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
    @PropertyName("witness_birthday")
    val witnessBirthday: Timestamp = Timestamp.now(),
    @Keep
    @JvmField
    @PropertyName("witness_phone")
    val witnessPhone: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_cellphone")
    val witnessCellphone: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_neighborhood")
    val witnessNeighborhood: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_street")
    val witnessStreet: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_city")
    val witnessCity: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_state")
    val witnessState: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_housenumber")
    val witnessHouseNumber: String = "",
    @Keep
    @JvmField
    @PropertyName("witness_zipcode")
    val witnessZipcode: String = "",

    @Keep
    @JvmField
    @PropertyName("has_takeaway")
    val hasTakeaway: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("takeaway_name")
    val takeawayName: String = "",
    @Keep
    @JvmField
    @PropertyName("takeaway_document")
    val takeawayDocument: String = "",

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
    @PropertyName("purchase_id")
    val purchaseId: String = "",

    @Keep
    @JvmField
    @PropertyName("payer_uids")
    val payerUids: List<String> = emptyList(),
    @Keep
    @JvmField
    @PropertyName("payer_documents")
    val payerDocuments: List<String> = emptyList(),

//    @Keep
//    @JvmField
//    @PropertyName("products")
//    val products: FSProductsSold = FSProductsSold(),

    @Keep
    @JvmField
    @PropertyName("has_own_frames")
    val hasOwnFrames: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("left_products")
    val leftProducts: FSProductSoldEyeSet = FSProductSoldEyeSet(),
    @Keep
    @JvmField
    @PropertyName("right_products")
    val rightProducts: FSProductSoldEyeSet = FSProductSoldEyeSet(),
    @Keep
    @JvmField
    @PropertyName("frames_products")
    val framesProducts: FSProductSoldFramesDescription = FSProductSoldFramesDescription(),
    @Keep
    @JvmField
    @PropertyName("misc_products")
    val miscProducts: List<FSProductSoldDescription> = emptyList(),

    // Denormilized prescription data
    @Keep
    @JvmField
    @PropertyName("prescription_id")
    val prescriptionId: String = "",

    @Keep
    @JvmField
    @PropertyName("is_copy")
    val isCopy: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("professional_name")
    val professionalName: String = "",
    @Keep
    @JvmField
    @PropertyName("professional_id")
    val professionalId: String = "",
    @Keep
    @JvmField
    @PropertyName("prescription_date")
    val prescriptionDate: Timestamp = Timestamp.now(),

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

    // Denormalized measuring and prescription data
    @Keep
    @JvmField
    @PropertyName("l_positioning_id")
    val lPositioningId: String = "",
    @Keep
    @JvmField
    @PropertyName("r_positioning_id")
    val rPositioningId: String = "",

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
    @PropertyName("l_ipd")
    val lIpd: Double = 0.0,
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
    @PropertyName("doc_version")
    val docVersion: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val isEditable: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("left_to_pay")
    val leftToPay: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("total")
    val total:  Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("total_paid")
    val totalPaid:  Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("total_discount")
    val totalDiscount:  Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

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
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy:  String = "",
)
