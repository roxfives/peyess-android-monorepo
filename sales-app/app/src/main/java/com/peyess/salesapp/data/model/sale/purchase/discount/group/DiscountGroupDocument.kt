package com.peyess.salesapp.data.model.sale.purchase.discount.group

import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.group.standard.StandardDiscountDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.set.DiscountSetDocument
import java.time.ZonedDateTime

data class DiscountGroupDocument(
    val id: String = "",
    val storeId: String = "",

    val name: String = "",
    val description: String = "",
    val allowedGeneral: Boolean = true,
    val general: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val standard: StandardDiscountDocument = StandardDiscountDocument(),
    val discounts: DiscountDescriptionDocument = DiscountDescriptionDocument(),
    val sets: DiscountSetDocument = DiscountSetDocument(),

    val docVersion: Int = 0,
    val isEditable: Boolean = false,
    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",
    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",
)
