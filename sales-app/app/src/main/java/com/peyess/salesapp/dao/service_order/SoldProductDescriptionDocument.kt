package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

data class SoldProductDescriptionDocument(
    val id: String? = "",

    val units: Int = 0,

    val nameDisplay: String = "",

    val price: Double = 0.0,
    val discount: FSDiscountDescription = FSDiscountDescription(),
)