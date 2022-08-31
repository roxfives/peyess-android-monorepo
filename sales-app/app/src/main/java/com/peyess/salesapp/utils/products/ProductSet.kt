package com.peyess.salesapp.utils.products

import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity

data class ProductSet(
    val lens: LocalLensEntity = LocalLensEntity(),
    val coloring: LocalColoringEntity = LocalColoringEntity(),
    val treatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val frames: FramesEntity = FramesEntity(),
)
