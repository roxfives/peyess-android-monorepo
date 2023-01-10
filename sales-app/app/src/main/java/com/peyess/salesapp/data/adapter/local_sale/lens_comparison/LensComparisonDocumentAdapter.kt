package com.peyess.salesapp.data.adapter.local_sale.lens_comparison

import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity

fun LensComparisonDocument.toLensComparisonEntity(): LensComparisonEntity {
    return LensComparisonEntity(
        id = id,
        soId = soId,
        originalLensId = originalLensId,
        originalColoringId = originalColoringId,
        originalTreatmentId = originalTreatmentId,
        comparisonLensId = comparisonLensId,
        comparisonColoringId = comparisonColoringId,
        comparisonTreatmentId = comparisonTreatmentId,
    )
}
