package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.model.prescription.FSPrescriptionUpdate

fun FSPrescriptionUpdate.toUpdateMap(): Map<String, Any> {
    return mapOf(
        "emitted" to emitted,
        "type_id" to typeId,
        "type_desc" to typeDesc,
        "is_copy" to isCopy,
        "patient_uid" to patientUid,
        "patient_document" to patientDocument,
        "patient_name" to patientName,
        "professional_document" to professionalDocument,
        "professional_name" to professionalName,
        "lens_category_id" to lensTypeCategoryId,
        "lens_category" to lensTypeCategory,
        "has_prism" to hasPrism,
        "has_addition" to hasAddition,

        "l_ipd" to lIpd,
        "l_bridge" to lBridge,
        "l_bridge_hoop" to lBridgeHoop,
        "l_h_hoop" to lHHoop,
        "l_he" to lHe,
        "l_v_hoop" to lVHoop,
        "l_diameter" to lDiameter,
        "l_cylinder" to lCylinder,
        "l_spherical" to lSpherical,
        "l_axis_degree" to lAxisDegree,
        "l_addition" to lAddition,
        "l_prism_axis" to lPrismAxis,
        "l_prism_degree" to lPrismDegree,
        "l_prism_pos" to lPrismPos,

        "r_ipd" to rIpd,
        "r_bridge" to rBridge,
        "r_bridge_hoop" to rBridgeHoop,
        "r_h_hoop" to rHHoop,
        "r_he" to rHe,
        "r_v_hoop" to rVHoop,
        "r_diameter" to rDiameter,
        "r_cylinder" to rCylinder,
        "r_spherical" to rSpherical,
        "r_axis_degree" to rAxisDegree,
        "r_addition" to rAddition,
        "r_prism_axis" to rPrismAxis,
        "r_prism_degree" to rPrismDegree,
        "r_prism_pos" to rPrismPos,

        "observation" to observation,

        "updated" to updated,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
