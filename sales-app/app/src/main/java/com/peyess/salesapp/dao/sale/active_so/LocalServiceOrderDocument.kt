package com.peyess.salesapp.dao.sale.active_so

import com.peyess.salesapp.typing.lens.LensTypeCategoryName

data class LocalServiceOrderDocument(
    val id: String = "",
    val hasPrescription: Boolean = true,
    val saleId: String = "",
    val clientName: String = "",
    val lensTypeCategoryName: LensTypeCategoryName = LensTypeCategoryName.None,
    val isLensTypeMono: Boolean = false,
)

