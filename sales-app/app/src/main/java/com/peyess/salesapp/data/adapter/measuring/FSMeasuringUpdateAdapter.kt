package com.peyess.salesapp.data.adapter.measuring

import com.peyess.salesapp.data.model.measuring.FSMeasuringUpdate

fun FSMeasuringUpdate.toUpdateMap(): Map<String, Any> {
    return mapOf(
        "taken_by_uid" to takenByUid,
        "patient_uid" to patientUid,
        "patient_document" to patientDocument,
        "patient_name" to patientName,
        "base_size" to baseSize,
        "base_height" to baseHeight,
        "top_angle" to topAngle,
        "top_point" to topPoint,
        "bottom_angle" to bottomAngle,
        "bottom_point" to bottomPoint,
        "bridge" to bridge,
        "diameter" to diameter,
        "vertical_check" to verticalCheck,
        "vertical_hoop" to verticalHoop,
        "horizontal_bridge_hoop" to horizontalBridgeHoop,
        "horizontal_check" to horizontalCheck,
        "horizontal_hoop" to horizontalHoop,
        "middle_check" to middleCheck,
        "euler_angle_x" to eulerAngleX,
        "euler_angle_y" to eulerAngleY,
        "euler_angle_z" to eulerAngleZ,
        "ipd" to ipd,
        "he" to he,
        "ho" to ho,
        "ve" to ve,
        "vu" to vu,
        "fixed_horizontal_bridge_hoop" to fixedHorizontalBridgeHoop,
        "fixed_bridge" to fixedBridge,
        "fixed_ipd" to fixedIpd,
        "fixed_h_hoop" to fixedHHoop,
        "fixed_he" to fixedHe,
        "fixed_v_hoop" to fixedVHoop,
        "fixed_diameter" to fixedDiameter,
        "updated" to updated,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
