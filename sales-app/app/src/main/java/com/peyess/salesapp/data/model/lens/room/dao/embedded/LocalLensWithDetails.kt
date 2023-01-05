package com.peyess.salesapp.data.model.lens.room.dao.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetailsDBView

data class LocalLensWithDetails(
    @Embedded
    val lens: LocalLensWithDetailsDBView = LocalLensWithDetailsDBView(),

    @Relation(
        parentColumn = "id",
        entityColumn = "lens_id",
        entity = LocalLensExplanationEntity::class
    )
    val explanations: List<LocalLensExplanationEntity> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "lens_id"
    )
    val disponibilities: List<LocalLensDisponibilityEntity> = emptyList(),

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            parentColumn = "lens_id",
            entityColumn = "alt_height_id",
            value = LocalLensAltHeightCrossRef::class,
        ),
    )
    val altHeights: List<LocalLensAltHeightEntity> = emptyList(),
)