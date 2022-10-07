package com.peyess.salesapp.data.model.measuring

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSMeasuring(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

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
    @PropertyName("prescription_id")
    val prescriptionId: String = "",
    @Keep
    @JvmField
    @PropertyName("positioning_id")
    val positioningId: String = "",

    @Keep
    @JvmField
    @PropertyName("taken_by_uid")
    val takenByUid: String = "",

    @Keep
    @JvmField
    @PropertyName("service_orders")
    val serviceOrders: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("correction_model_version")
    val correctionModelVersion: String = "",

    @Keep
    @JvmField
    @PropertyName("patient_uid")
    val patientUid: String = "",
    @Keep
    @JvmField
    @PropertyName("patient_document")
    val patientDocument: String = "",
    @Keep
    @JvmField
    @PropertyName("patient_name")
    val patientName: String = "",

    @Keep
    @JvmField
    @PropertyName("eye")
    val eye: String = "",

    @Keep
    @JvmField
    @PropertyName("base_size")
    val baseSize: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("top_angle")
    val topAngle: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("top_point")
    val topPoint: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("bottom_angle")
    val bottomAngle: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("bottom_point")
    val bottomPoint: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("bridge")
    val bridge: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("diameter")
    val diameter: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("vertical_check")
    val verticalCheck: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("vertical_hoop")
    val verticalHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("horizontal_bridge_hoop")
    val horizontalBridgeHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("horizontal_check")
    val horizontalCheck: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("horizontal_hoop")
    val horizontalHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("middle_check")
    val middleCheck: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("euler_angle_x")
    val eulerAngleX: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("euler_angle_y")
    val eulerAngleY: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("euler_angle_z")
    val eulerAngleZ: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("ipd")
    val ipd: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("he")
    val he: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("ho")
    val ho: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("ve")
    val ve: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("vu")
    val vu: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("fixed_bridge")
    val fixedBridge: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("fixed_ipd")
    val fixedIpd: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("fixed_h_hoop")
    val fixedHHoop: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("fixed_he")
    val fixedHe: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("fixed_v_hoop")
    val fixedVHoop: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("doc_version")
    val doc_version: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val is_editable: Boolean = false,

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
