package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedClient

fun FSDenormalizedClient.toDenormalizedClientDocument(): DenormalizedClientDocument {
    return DenormalizedClientDocument(
        uid = uid,
        document = document,
        name = name,
    )
}