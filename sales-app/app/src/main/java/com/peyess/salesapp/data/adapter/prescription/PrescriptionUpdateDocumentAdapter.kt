package com.peyess.salesapp.data.adapter.prescription

import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument

fun PrescriptionUpdateDocument.toUpdateMap(): Map<String, Any> {
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
        "has_prism" to hasPrism,
        "has_addition" to hasAddition,
        "l_cylinder" to lCylinder,
        "l_spherical" to lSpherical,
        "l_axis_degree" to lAxisDegree,
        "l_addition" to lAddition,
        "l_prism_axis" to lPrismAxis,
        "l_prism_degree" to lPrismDegree,
        "l_prism_pos" to lPrismPos,
        "r_cylinder" to rCylinder,
        "r_spherical" to rSpherical,
        "r_axis_degree" to rAxisDegree,
        "r_addition" to rAddition,
        "r_prism_axis" to rPrismAxis,
        "r_prism_degree" to rPrismDegree,
        "r_prism_pos" to rPrismPos,
        "updated" to updated,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
