package com.peyess.salesapp.data.adapter.client_legal

import com.peyess.salesapp.data.model.client_legal.ClientLegalDocument
import com.peyess.salesapp.data.model.client_legal.FSClientLegal
import com.peyess.salesapp.utils.time.toZonedDateTime

fun FSClientLegal.toClientLegalDocument(): ClientLegalDocument {
    return ClientLegalDocument(
        hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
        methodPromotionalMessages = methodPromotionalMessages,
        doc_version = doc_version,
        is_editable = is_editable,
        created = created.toZonedDateTime(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toZonedDateTime(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}

fun FSClientLegal.toMap(): Map<String, Any> {
    return mapOf(
        "has_accepted_promotional_messages" to hasAcceptedPromotionalMessages,
        "method_promotional_messages" to methodPromotionalMessages,
        "doc_version" to doc_version,
        "is_editable" to is_editable,
        "created" to created,
        "created_by" to createdBy,
        "create_allowed_by" to createAllowedBy,
        "updated" to updated,
        "updated_by" to updatedBy,
        "update_allowed_by" to updateAllowedBy,
    )
}
