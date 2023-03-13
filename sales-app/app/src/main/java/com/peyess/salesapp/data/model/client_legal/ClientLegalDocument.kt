package com.peyess.salesapp.data.model.client_legal

import java.time.ZonedDateTime

data class ClientLegalDocument(
    val hasAcceptedPromotionalMessages: Boolean = false,
    val methodPromotionalMessages: String = ClientLegalMethod.SalesAppCreateAccount.toName(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
