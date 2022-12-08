package com.peyess.salesapp.data.model.lens.description

import com.google.firebase.Timestamp

data class LensDescriptionDocument(
    val id: String = "",

    val name: String = "",

    val priority: Double = 0.0,

    val familiesIds: List<String> = emptyList(),

    val doc_version: Int = 0,

    val is_editable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)