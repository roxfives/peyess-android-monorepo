package com.peyess.salesapp.data.model.lens.room.treatment

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTreatmentCrossRef

private const val localLensTreatmentEntity = LocalLensTreatmentEntity.tableName
private const val localLensTreatmentCrossRef = LocalLensTreatmentCrossRef.tableName

@DatabaseView(
    viewName = LocalLensTreatmentPriceDBView.viewName,
    value = """
        SELECT treatment.id AS id,
                treatment.brand AS brand,
                treatment.design AS design,
                treatment.is_coloring_required AS isColoringRequired,
                treatment.is_enabled AS isEnabled,
                treatment.is_local_enabled AS isLocalEnabled,
                treatment.observation AS observation,
                treatment.priority AS priority,
                treatment.shipping_time AS shippingTime,
                treatment.specialty AS specialty,
                treatment.supplier_id AS supplierId,
                treatment.supplier AS supplier,
                treatment.warning AS warning,
                _junction.lens_id AS lensId, 
                _junction.price AS price
            FROM $localLensTreatmentCrossRef AS _junction
            LEFT JOIN $localLensTreatmentEntity AS treatment
                ON treatment.id = _junction.treatment_id
    """,
)
data class LocalLensTreatmentPriceDBView(
    val id: String = "",
    val brand: String = "",
    val design: String = "",
    val isColoringRequired: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val supplierId: String = "",
    val supplier: String = "",
    val warning: String = "",

    val lensId: String = "",
    val price: Double = 0.0,
) {
    companion object {
        const val viewName = "local_lens_treatment_price"
    }
}
