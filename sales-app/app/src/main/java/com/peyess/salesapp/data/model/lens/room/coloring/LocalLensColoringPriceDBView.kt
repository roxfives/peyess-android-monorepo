package com.peyess.salesapp.data.model.lens.room.coloring

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensColoringCrossRef

private const val localLensColoringEntity = LocalLensColoringEntity.tableName
private const val localLensColoringCrossRef = LocalLensColoringCrossRef.tableName

@DatabaseView(
    viewName = LocalLensColoringPriceDBView.viewName,
    value = """
        SELECT coloring.id as id,
                coloring.brand as brand,
                coloring.design as design,
                coloring.has_medical as hasMedical,
                coloring.is_enabled as isEnabled,
                coloring.is_local_enabled as isLocalEnabled,
                coloring.is_treatment_required as isTreatmentRequired,
                coloring.observation as observation,
                coloring.priority as priority,
                coloring.shipping_time as shippingTime,
                coloring.specialty as specialty,
                coloring.supplier_id as supplierId,
                coloring.supplier as supplier,
                coloring.type as type,
                coloring.warning as warning,
                _junction.lens_id AS lensId, 
                _junction.price AS price
            FROM $localLensColoringCrossRef AS _junction
            LEFT JOIN $localLensColoringEntity AS coloring
                ON coloring.id = _junction.coloring_id
    """,
)
data class LocalLensColoringPriceDBView(
    val id: String = "",
    val brand: String = "",
    val design: String = "",
    val hasMedical: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val isTreatmentRequired: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val supplierId: String = "",
    val supplier: String = "",
    val type: String = "",
    val warning: String = "",

    val lensId: String = "",
    val price: Double = 0.0,
) {
    companion object {
        const val viewName = "local_lens_coloring_price"
    }
}