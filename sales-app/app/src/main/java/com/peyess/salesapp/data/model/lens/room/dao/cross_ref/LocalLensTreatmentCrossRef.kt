package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = ["lens_id", "treatment_id"],
    tableName = LocalLensTreatmentCrossRef.tableName,
)
data class LocalLensTreatmentCrossRef(
    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "treatment_id")
    val treatmentId: String = "",
) {
    companion object {
        const val tableName = "local_lenses_treatment_cross_ref"
    }
}
