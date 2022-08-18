package com.peyess.salesapp.dao.products.firestore.lens_treatment

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName

data class FSLensTreatment(
    @Keep
    @JvmField
    @PropertyName("specialty")
    val specialty: String = "",

    @Keep
    @JvmField
    @PropertyName("is_coloring_required")
    val isColoringRequired: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("is_generic")
    val isGeneric: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("cost")
    val cost: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("price")
    val price: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("suggested_price")
    val suggestedPrice: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("shipping_time")
    val shippingTime: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("observation")
    val observation: String = "",

    @Keep
    @JvmField
    @PropertyName("warning")
    val warning: String = "",

    @Keep
    @JvmField
    @PropertyName("brand")
    val brand: String = "",

    @Keep
    @JvmField
    @PropertyName("brand_id")
    val brandId: String = "",

    @Keep
    @JvmField
    @PropertyName("DESIGN")
    val design: String = "",

    @Keep
    @JvmField
    @PropertyName("design_id")
    val designId: String = "",

    @Keep
    @JvmField
    @PropertyName("supplier_picture")
    val supplierPicture: String = "",

    @Keep
    @JvmField
    @PropertyName("SUPPLIER")
    val supplier: String = "",

    @Keep
    @JvmField
    @PropertyName("supplier_id")
    val supplierId: String = "",

    @Keep
    @JvmField
    @PropertyName("is_manufacturing_local")
    val isManufacturingLocal: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_enabled")
    val isEnabled: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("reason_disabled")
    val reasonDisabled: String = "",
)