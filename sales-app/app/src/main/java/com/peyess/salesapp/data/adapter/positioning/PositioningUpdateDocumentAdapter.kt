package com.peyess.salesapp.data.adapter.positioning

import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument

fun PositioningUpdateDocument.toMappingUpdate(): Map<String, Any> {
    return mapOf(
        "taken_by_uid" to takenByUid,
        "patient_uid" to patientUid,
        "patient_document" to patientDocument,
        "patient_name" to patientName,
        "base_left" to baseLeft,
        "base_left_rotation" to baseLeftRotation,
        "base_right" to baseRight,
        "base_right_rotation" to baseRightRotation,
        "base_top" to baseTop,
        "base_bottom" to baseBottom,
        "top_point_length" to topPointLength,
        "top_point_rotation" to topPointRotation,
        "bottom_point_length" to bottomPointLength,
        "bottom_point_rotation" to bottomPointRotation,
        "bridge_pivot" to bridgePivot,
        "check_bottom" to checkBottom,
        "check_top" to checkTop,
        "check_left" to checkLeft,
        "check_left_rotation" to checkLeftRotation,
        "check_middle" to checkMiddle,
        "check_right" to checkRight,
        "check_right_rotation" to checkRightRotation,
        "frames_bottom" to framesBottom,
        "frames_left" to framesLeft,
        "frames_right" to framesRight,
        "frames_top" to framesTop,
        "optic_center_radius" to opticCenterRadius,
        "optic_center_x" to opticCenterX,
        "optic_center_y" to opticCenterY,
        "proportion_to_picture_horizontal" to proportionToPictureHorizontal,
        "proportion_to_picture_vertical" to proportionToPictureVertical,
        "device" to device,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
