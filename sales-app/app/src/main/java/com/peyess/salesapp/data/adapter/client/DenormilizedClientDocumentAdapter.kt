package com.peyess.salesapp.data.adapter.client

import android.net.Uri
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.FSDenormalizedClient

fun FSDenormalizedClient.toDenormalizedClientDocument(): DenormalizedClientDocument {
    return DenormalizedClientDocument(
        uid = uid,
        document = document,
        name = name,
        picture = Uri.parse(picture),
    )
}