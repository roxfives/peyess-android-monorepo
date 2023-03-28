package com.peyess.salesapp.data.adapter.edit_service_order.lens_comparison

import com.peyess.salesapp.data.model.edit_service_order.lens_comparison.EditLensComparisonEntity
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument

fun EditLensComparisonEntity.toLensComparisonDocument(): LensComparisonDocument {
    return LensComparisonDocument(
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