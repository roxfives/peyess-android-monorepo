package com.peyess.salesapp.screen.sale.lens_comparison.model

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument

data class LensPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val lensPicked: StoreLensWithDetailsDocument = StoreLensWithDetailsDocument(),
)
