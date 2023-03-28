package com.peyess.salesapp.data.adapter.purchase.fee

import com.peyess.salesapp.data.model.sale.purchase.fee.FSFeeDescription
import com.peyess.salesapp.data.model.sale.purchase.fee.FeeDescriptionDocument

fun FeeDescriptionDocument.toFSFeeDescription(): FSFeeDescription {
    return FSFeeDescription(
        method = method.toName(),
        value = value.toDouble(),
    )
}
