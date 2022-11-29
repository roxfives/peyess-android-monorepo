package com.peyess.salesapp.data.model.sale.purchase.discount.group

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.sale.purchase.discount.description.FSDiscountDescription
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.FSStandardDiscount
import com.peyess.salesapp.data.model.sale.purchase.discount.set.FSDiscountSet

@Keep
@IgnoreExtraProperties
data class FSDiscountGroup(

    @Keep
    @JvmField
    @PropertyName("id")
    val id: String = "",

    @Keep
    @JvmField
    @PropertyName("store_id")
    val storeId: String = "",

    @Keep
    @JvmField
    @PropertyName("name")
    val name: String = "",

    @Keep
    @JvmField
    @PropertyName("description")
    val description: String = "",

    @Keep
    @JvmField
    @PropertyName("allowed_general")
    val allowedGeneral: Boolean = true,

    @Keep
    @JvmField
    @PropertyName("general")
    val general: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("standard")
    val standard: FSStandardDiscount = FSStandardDiscount(),

    @Keep
    @JvmField
    @PropertyName("discounts")
    val discounts: FSDiscountDescription = FSDiscountDescription(),

    @Keep
    @JvmField
    @PropertyName("sets")
    val sets: FSDiscountSet = FSDiscountSet(),

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
