package com.peyess.salesapp.feature.lens_comparison.model

import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument

data class LensPickResponse(
    val lensComparison: IndividualComparison = IndividualComparison(),
    val lensPicked: StoreLensWithDetailsDocument = StoreLensWithDetailsDocument(),
)
