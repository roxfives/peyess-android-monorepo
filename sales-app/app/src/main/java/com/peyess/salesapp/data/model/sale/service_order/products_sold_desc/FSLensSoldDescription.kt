package com.peyess.salesapp.data.model.sale.service_order.products_sold_desc

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription

@Keep
@IgnoreExtraProperties
data class FSLensSoldDescription(
    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

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
    @PropertyName("is_discounted")
    val isDiscounted: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_included")
    val isIncluded: Boolean = false,

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

    @Keep
    @JvmField
    @PropertyName("supplier_id")
    val supplierId: String = "",
    @Keep
    @JvmField
    @PropertyName("supplier_name")
    val supplierName: String = "",

    @Keep
    @JvmField
    @PropertyName("with_alt_height")
    val withAltHeight: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("alt_height_id")
    val altHeightId: String = "",
    @Keep
    @JvmField
    @PropertyName("alt_height_desc")
    val altHeightDesc: String = "",

    @Keep
    @JvmField
    @PropertyName("with_treatment")
    val withTreatment: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("with_coloring")
    val withColoring: Boolean = false,
    @Keep
    @JvmField
    @PropertyName("with_cut")
    val withCut: Boolean = false,
)
