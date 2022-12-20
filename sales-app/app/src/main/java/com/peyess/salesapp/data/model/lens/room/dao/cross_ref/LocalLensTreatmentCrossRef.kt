package com.peyess.salesapp.data.model.lens.room.dao.cross_ref

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["lens_id", "treatment_id"])
data class LocalLensTreatmentCrossRef(
    @ColumnInfo(name = "lens_id")
    val lensId: String = "",

    @ColumnInfo(name = "treatment_id")
    val treatmentId: String = "",
)
