package com.peyess.salesapp.data.model.positioning

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSPositioning(
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
    @PropertyName("prescription_id")
    val prescriptionId: String = "",

    @JvmField
    @Keep
    @PropertyName("taken_by_uid")
    val takenByUid: String = "",

    @JvmField
    @Keep
    @PropertyName("service_orders")
    val serviceOrders: List<String> = emptyList(),

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
    @PropertyName("eye")
    val eye: String = "",

    @JvmField
    @Keep
    @PropertyName("base_left")
    val baseLeft: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("base_left_rotation")
    val baseLeftRotation: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("base_right")
    val baseRight: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("base_right_rotation")
    val baseRightRotation: Double = 0.0,

    @JvmField
    @PropertyName("base_top")
    val baseTop: Double = 0.0,
    @JvmField
    @PropertyName("base_bottom")
    val baseBottom: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("top_point_length")
    val topPointLength: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("top_point_rotation")
    val topPointRotation: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("bottom_point_length")
    val bottomPointLength: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("bottom_point_rotation")
    val bottomPointRotation: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("bridge_pivot")
    val bridgePivot: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_bottom")
    val checkBottom: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_top")
    val checkTop: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_left")
    val checkLeft: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_left_rotation")
    val checkLeftRotation: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_middle")
    val checkMiddle: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_right")
    val checkRight: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("check_right_rotation")
    val checkRightRotation: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("frames_bottom")
    val framesBottom: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("frames_left")
    val framesLeft: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("frames_right")
    val framesRight: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("frames_top")
    val framesTop: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("optic_center_radius")
    val opticCenterRadius: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("optic_center_x")
    val opticCenterX: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("optic_center_y")
    val opticCenterY: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("proportion_to_picture_horizontal")
    val proportionToPictureHorizontal: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("proportion_to_picture_vertical")
    val proportionToPictureVertical: Double = 0.0,

    @JvmField
    @Keep
    @PropertyName("device")
    val device: String = "",

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