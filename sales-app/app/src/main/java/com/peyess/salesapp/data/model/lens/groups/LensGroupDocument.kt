package com.peyess.salesapp.data.model.lens.groups

import java.time.ZonedDateTime

data class LensGroupDocument(
    val id: String = "",
    val name: String = "",

    val priority: Double = 0.0,

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
