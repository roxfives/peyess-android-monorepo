package com.peyess.salesapp.data.model.lens.room.coloring.embedded

import androidx.room.Embedded
import androidx.room.Relation
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringPriceDBView
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity

data class LocalLensColoringWithExplanationsEntity(
    @Embedded
    val coloring: LocalLensColoringPriceDBView = LocalLensColoringPriceDBView(),

    @Relation(
        parentColumn = "id",
        entityColumn = "coloring_id",
    )
    val explanations: List<LocalLensColoringExplanationEntity> = emptyList(),
)