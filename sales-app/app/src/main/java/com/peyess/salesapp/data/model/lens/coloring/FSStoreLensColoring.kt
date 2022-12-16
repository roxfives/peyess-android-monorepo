package com.peyess.salesapp.data.model.lens.coloring

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensColoring(
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
    @PropertyName("has_medical")
    val hasMedical: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_treatment_required")
    val isTreatmentRequired: Boolean = false,

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
    @PropertyName("price")
    val price: Double = 0.0,

    @Keep
    @JvmField
    @PropertyName("cost")
    val cost: Double = 0.0,

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
    @PropertyName("design")
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
    @PropertyName("supplier")
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

    @Keep
    @JvmField
    @PropertyName("display")
    val display: List<String> = emptyList(),

    @Keep
    @JvmField
    @PropertyName("doc_version")
    val doc_version: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val is_editable: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)