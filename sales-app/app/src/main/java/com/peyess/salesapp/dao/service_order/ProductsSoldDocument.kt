package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

data class ProductsSoldDocument(
    val lenses: Map<String, SoldProductDescriptionDocument> = emptyMap(),
    val colorings: Map<String, SoldProductDescriptionDocument> = emptyMap(),
    val treatments: Map<String, SoldProductDescriptionDocument> = emptyMap(),

    val frames: SoldProductDescriptionDocument = SoldProductDescriptionDocument(),

    val misc: List<SoldProductDescriptionDocument> = emptyList(),
)
