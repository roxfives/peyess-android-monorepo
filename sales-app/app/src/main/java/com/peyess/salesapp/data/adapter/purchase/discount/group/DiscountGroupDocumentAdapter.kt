package com.peyess.salesapp.data.adapter.purchase.discount.group

import com.peyess.salesapp.data.adapter.purchase.discount.description.toFSDiscountDescription
import com.peyess.salesapp.data.adapter.purchase.discount.group.standard.toFSStandardDiscount
import com.peyess.salesapp.data.model.sale.purchase.discount.group.DiscountGroupDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.FSDiscountGroup
import com.peyess.salesapp.utils.time.toTimestamp

fun DiscountGroupDocument.toFSDiscountGroup(): FSDiscountGroup {
    return FSDiscountGroup(
        id = id,
        storeId = storeId,

        name = name,
        description = description,
        allowedGeneral = allowedGeneral,
        general = general.toFSDiscountDescription(),
        standard = standard.toFSStandardDiscount(),
        discounts = discounts.toFSDiscountDescription(),

        isEditable = isEditable,
        docVersion = docVersion,
        created = created.toTimestamp(),
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updated = updated.toTimestamp(),
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}