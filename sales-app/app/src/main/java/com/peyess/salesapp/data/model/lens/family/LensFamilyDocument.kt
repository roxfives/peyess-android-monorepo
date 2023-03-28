package com.peyess.salesapp.data.model.lens.family

import com.google.firebase.Timestamp


data class LensFamilyDocument(
    val id: String = "",
    val name: String = "",

    val priority: String = "",

    val docVersion: Int = 0,
    val is_editable: Boolean = false,
    val created: Timestamp = Timestamp.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: Timestamp = Timestamp.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
