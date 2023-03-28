package com.peyess.salesapp.data.adapter.client

import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedClient

fun DenormalizedClientDocument.toFSDenormalizedClient(): FSDenormalizedClient {
    return FSDenormalizedClient(
        uid = uid,
        document = document,
        name = name,
    )
}