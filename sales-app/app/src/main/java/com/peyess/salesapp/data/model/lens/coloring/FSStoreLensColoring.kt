package com.peyess.salesapp.data.model.lens.coloring

import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSStoreLensColoring(
    @Keep
    @JvmField
    @PropertyName("brand")
    val brand: String = "",

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
    @PropertyName("design")
    val design: String = "",

    @Keep
    @JvmField
    @PropertyName("display")
    val display: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("explanations")
    val explanations: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("has_medical")
    val hasMedical: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_enabled")
    val isEnabled: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_local_enabled")
    val isLocalEnabled: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_treatment_required")
    val isTreatmentRequired: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("observation")
    val observation: String = "",

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("shipping_time")
    val shippingTime: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("specialty")
    val specialty: String = "",

    @Keep
    @JvmField
    @PropertyName("suggested_price")
    val suggestedPrice: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("supplier")
    val supplier: String = "",

    @Keep
    @JvmField
    @PropertyName("type")
    val type: String = "",

    @Keep
    @JvmField
    @PropertyName("warning")
    val warning: String = "",
)