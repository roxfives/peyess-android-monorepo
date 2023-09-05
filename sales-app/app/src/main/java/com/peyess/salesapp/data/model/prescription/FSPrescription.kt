package com.peyess.salesapp.data.model.prescription

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.typing.lens.LensTypeCategoryName

@Keep
@IgnoreExtraProperties
data class FSPrescription(
    @JvmField
    @Keep
    @PropertyName("id")
    val id: String = "",

    @JvmField
    @Keep
    @PropertyName("store_id")
    val storeId: String = "",

    @JvmField
    @Keep
    @PropertyName("store_ids")
    val storeIds: List<String> = emptyList(),

    @JvmField
    @Keep
    @PropertyName("emitted")
    val emitted: Timestamp = Timestamp.now(),

    @JvmField
    @Keep
    @PropertyName("type_id")
    val typeId: String = "",
    @JvmField
    @Keep
    @PropertyName("type_desc")
    val typeDesc: String = "",

    @JvmField
    @Keep
    @PropertyName("is_copy")
    val isCopy: Boolean = false,

    @JvmField
    @Keep
    @PropertyName("patient_uid")
    val patientUid: String = "",
    @JvmField
    @Keep
    @PropertyName("patient_document")
    val patientDocument: String = "",
    @JvmField
    @Keep
    @PropertyName("patient_name")
    val patientName: String = "",

    @JvmField
    @Keep
    @PropertyName("professional_document")
    val professionalDocument: String = "",
    @JvmField
    @Keep
    @PropertyName("professional_name")
    val professionalName: String = "",


    @JvmField
    @Keep
    @PropertyName("lens_category_id")
    val lensTypeCategoryId: String = "",
    @JvmField
    @Keep
    @PropertyName("lens_category")
    val lensTypeCategory: String = LensTypeCategoryName.None.toName(),

    @JvmField
    @Keep
    @PropertyName("has_prism")
    val hasPrism: Boolean = false,

    @JvmField
    @Keep
    @PropertyName("has_addition")
    val hasAddition: Boolean = false,

    @JvmField
    @Keep
    @PropertyName("pd")
    val pd: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("l_ipd")
    val lIpd: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_bridge")
    val lBridge: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_bridge_hoop")
    val lBridgeHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_h_hoop")
    val lHHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_he")
    val lHe: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_v_hoop")
    val lVHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_diameter")
    val lDiameter: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("l_cylinder")
    val lCylinder: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_spherical")
    val lSpherical: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_axis_degree")
    val lAxisDegree: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_addition")
    val lAddition: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_prism_axis")
    val lPrismAxis: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_prism_degree")
    val lPrismDegree: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("l_prism_pos")
    val lPrismPos: String = "",

    @JvmField
    @Keep
    @PropertyName("r_ipd")
    val rIpd: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_bridge")
    val rBridge: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_bridge_hoop")
    val rBridgeHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_h_hoop")
    val rHHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_he")
    val rHe: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_v_hoop")
    val rVHoop: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_diameter")
    val rDiameter: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("r_cylinder")
    val rCylinder: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_spherical")
    val rSpherical: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_axis_degree")
    val rAxisDegree: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_addition")
    val rAddition: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_prism_axis")
    val rPrismAxis: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_prism_degree")
    val rPrismDegree: Double = 0.0,
    @JvmField
    @Keep
    @PropertyName("r_prism_pos")
    val rPrismPos: String = "",

    @JvmField
    @Keep
    @PropertyName("observation")
    val observation: String = "",

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
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)