package com.peyess.salesapp.dao.service_order

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

@Keep
data class FSSoldProductDescription(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String? = "",

    @Keep
    @JvmField
    @PropertyName("units")
    val units: Int = 0,

    @Keep
    @JvmField
    @PropertyName("name_display")
    val nameDisplay: String = "",

    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("discount")
    val discount: FSDiscountDescription = FSDiscountDescription(),
)

fun FSSoldProductDescription.toDocument(): SoldProductDescriptionDocument {
    return SoldProductDescriptionDocument(
        id = id,
        units = units,
        nameDisplay = nameDisplay,
        price = price,
        discount = discount,
    )
}