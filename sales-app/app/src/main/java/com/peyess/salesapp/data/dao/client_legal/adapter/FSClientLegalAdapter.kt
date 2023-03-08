package com.peyess.salesapp.data.dao.client_legal.adapter

import com.peyess.salesapp.data.model.client_legal.FSClientLegal

fun FSClientLegal.toMap(): Map<String, Any> {
    return mapOf(
        "hasAcceptedPromotionalMessages" to hasAcceptedPromotionalMessages,
        "methodPromotionalMessages" to methodPromotionalMessages,
        "doc_version" to doc_version,
        "is_editable" to is_editable,
        "created" to created,
        "createdBy" to createdBy,
        "createAllowedBy" to createAllowedBy,
        "updated" to updated,
        "updatedBy" to updatedBy,
        "updateAllowedBy" to updateAllowedBy,
    )
}