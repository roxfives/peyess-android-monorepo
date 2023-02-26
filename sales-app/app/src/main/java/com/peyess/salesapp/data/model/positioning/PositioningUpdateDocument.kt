package com.peyess.salesapp.data.model.positioning

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class PositioningUpdateDocument(
    @JvmField
    @Keep
    @PropertyName("taken_by_uid")
    val takenByUid: String = "",

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
    @Keep
    @PropertyName("base_top")
    val baseTop: Double = 0.0,
    @JvmField
    @Keep
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
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),
    @JvmField
    @Keep
    @PropertyName("updated_by")
    val updatedBy: String = "",
    @JvmField
    @Keep
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)
