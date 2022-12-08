package com.peyess.salesapp.data.model.lens.treatment

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

data class LensTreatmentDocument(
    val id: String = "",

    val specialty: String = "",

    val isColoringRequired: Boolean = false,

    val priority: Double = 0.0,

    val isGeneric: Boolean = false,

    val cost: Double = 0.0,

    val price: Double = 0.0,

    val suggestedPrice: Double = 0.0,

    val shippingTime: Double = 0.0,

    val observation: String = "",

    val warning: String = "",

    val brand: String = "",

    val brandId: String = "",

    val design: String = "",

    val designId: String = "",

    val supplierPicture: String = "",

    val supplier: String = "",

    val supplierId: String = "",

    val isManufacturingLocal: Boolean = false,

    val isEnabled: Boolean = false,

    val reasonDisabled: String = "",

    val docVersion: Int = 0,
    val is_editable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
