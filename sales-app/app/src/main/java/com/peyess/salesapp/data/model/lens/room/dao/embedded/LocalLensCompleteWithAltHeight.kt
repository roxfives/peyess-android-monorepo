package com.peyess.salesapp.data.model.lens.room.dao.embedded

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensFullUnionWithHeightAndLensTypeDBView

data class LocalLensCompleteWithAltHeight(
    @Embedded
    val lens: LocalLensFullUnionWithHeightAndLensTypeDBView =
        LocalLensFullUnionWithHeightAndLensTypeDBView(),

    @Relation(
        parentColumn = "id",
        entityColumn = "lens_id",
        entity = LocalLensExplanationEntity::class
    )
    val explanations: List<LocalLensExplanationEntity> = emptyList(),
)
