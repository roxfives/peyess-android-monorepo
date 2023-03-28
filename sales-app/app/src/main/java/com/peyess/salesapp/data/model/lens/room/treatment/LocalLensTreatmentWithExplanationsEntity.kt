package com.peyess.salesapp.data.model.lens.room.treatment

import androidx.room.Embedded
import androidx.room.Relation

data class LocalLensTreatmentWithExplanationsEntity(
    @Embedded
    val treatment: LocalLensTreatmentEntity = LocalLensTreatmentEntity(),

    @Relation(
        parentColumn = "id",
        entityColumn = "treatment_id",
    )
    val explanations: List<LocalLensTreatmentExplanationEntity> = emptyList(),
)