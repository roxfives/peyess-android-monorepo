package com.peyess.salesapp.data.model.sale.purchase


data class DenormalizedServiceOrderDescDocument(
    val lenses: List<DenormalizedPurchaseDescriptionDocument> = emptyList(),
    val colorings: List<DenormalizedPurchaseDescriptionDocument> = emptyList(),
    val treatments: List<DenormalizedPurchaseDescriptionDocument> = emptyList(),

    val frames: DenormalizedPurchaseDescriptionDocument = DenormalizedPurchaseDescriptionDocument(),

    val misc: List<DenormalizedPurchaseDescriptionDocument> = emptyList(),
)
