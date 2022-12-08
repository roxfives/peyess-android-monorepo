package com.peyess.salesapp.data.model.lens.disponibility

import com.google.firebase.Timestamp

data class DisponibilityDocument(

    val name: String = "",

    val diam: Double = 0.0,

    val maxCyl: Double = 0.0,

    val minCyl: Double = 0.0,

    val maxSph: Double = 0.0,

    val minSph: Double = 0.0,

    val maxAdd: Double = 0.0,

    val minAdd: Double = 0.0,

    val hasPrism: Boolean = false,

    val prism: Double = 0.0,

    val prismPrice: Double = 0.0,

    val prismCost: Double = 0.0,

    val separatePrism: Boolean = false,

    val needsCheck: Boolean = false,

    val sumRule: Boolean = false,

    val manufacturers: Map<String, DispManufacturerDocument> = emptyMap(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)