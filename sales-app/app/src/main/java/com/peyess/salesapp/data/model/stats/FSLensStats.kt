package com.peyess.salesapp.data.model.stats

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@Keep
@IgnoreExtraProperties
data class FSLensStats(
    @Keep
    @JvmField
    @PropertyName("total")
    val total: Int = 0,
    @Keep
    @JvmField
    @PropertyName("disabled")
    val disabled: Int = 0,

    @Keep
    @JvmField
    @PropertyName("supplier")
    val supplier: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("supplier_disabled")
    val supplierDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("group")
    val group: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("group_disabled")
    val groupDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("specialty")
    val specialty: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("specialty_disabled")
    val specialtyDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("tech")
    val tech: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("tech_disabled")
    val techDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("type")
    val type: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("type_disabled")
    val typeDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("material")
    val material: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("material_disabled")
    val materialDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("brand")
    val brand: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("brand_disabled")
    val brandDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("design")
    val design: Map<String, Int> = emptyMap(),
    @Keep
    @JvmField
    @PropertyName("design_disabled")
    val designDisabled: Map<String, Int> = emptyMap(),

    @Keep
    @JvmField
    @PropertyName("priority")
    val priority: String = "",

    @Keep
    @JvmField
    @PropertyName("doc_version")
    val docVersion: Int = 0,

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val isEditable: Boolean = false,

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
