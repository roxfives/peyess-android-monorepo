package com.peyess.salesapp.data.model.lens.alt_height

import java.time.ZonedDateTime

data class LensAltHeightDocument(
    val name: String = "",

    val name_display: String = "",

    val value: Double = 0.0,

    val observation: String = "",

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)