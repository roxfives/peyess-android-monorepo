package com.peyess.salesapp.data.model.lens.coloring

import java.time.ZonedDateTime

data class StoreLensColoringDocument(
    val specialty: String = "",

    val isColoringRequired: Boolean = false,

    val hasMedical: Boolean = false,

    val isTreatmentRequired: Boolean = false,

    val priority: Double = 0.0,

    val isGeneric: Boolean = false,

    val price: Double = 0.0,

    val cost: Double = 0.0,

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

    val display: List<String> = emptyList(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)