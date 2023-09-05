package com.peyess.salesapp.data.model.sale.service_order.products_sold_desc

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSProductSoldFramesDescription(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("design")
    val design: String = "",
    @Keep
    @JvmField
    @PropertyName("code")
    val code: String = "",
    @Keep
    @JvmField
    @PropertyName("reference")
    val reference: String = "",
    @Keep
    @JvmField
    @PropertyName("info")
    val info: String = "",
    @Keep
    @JvmField
    @PropertyName("color")
    val color: String = "",
    @Keep
    @JvmField
    @PropertyName("style")
    val style: String = "",
    @Keep
    @JvmField
    @PropertyName("type")
    val type: String = "",

    @Keep
    @JvmField
    @PropertyName("units")
    val units: Int = 0,
    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,
    @Keep
    @JvmField
    @PropertyName("discount")
    val discount: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("accessory_per_unit")
    val accessoryPerUnit: List<FSAccessoryItem> = emptyList(),
)
