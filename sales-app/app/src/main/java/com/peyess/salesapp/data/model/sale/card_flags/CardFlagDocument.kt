package com.peyess.salesapp.data.model.sale.card_flags

import android.net.Uri
import java.time.ZonedDateTime

data class CardFlagDocument(
    val id: String = "",

    val name: String = "",
    val icon: Uri = Uri.EMPTY,

    val docVersion: Int = 0,
    val isEditable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val updated: ZonedDateTime = ZonedDateTime.now(),

    val createdBy: String = "",
    val updatedBy: String = "",
    val createAllowedBy: String = "",
    val updateAllowedBy: String = "",
)
