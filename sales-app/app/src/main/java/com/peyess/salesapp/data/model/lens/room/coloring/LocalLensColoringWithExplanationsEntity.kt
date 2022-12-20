package com.peyess.salesapp.data.model.lens.room.coloring

import androidx.room.Embedded
import androidx.room.Relation

data class LocalLensColoringWithExplanationsEntity(
    @Embedded
    val coloring: LocalLensColoringEntity = LocalLensColoringEntity(),

    @Relation(
        parentColumn = "id",
        entityColumn = "coloring_id",
    )
    val explanations: List<LocalLensColoringExplanationEntity> = emptyList(),
)