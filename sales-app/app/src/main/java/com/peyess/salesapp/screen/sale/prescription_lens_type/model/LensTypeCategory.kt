package com.peyess.salesapp.screen.sale.prescription_lens_type.model

import com.peyess.salesapp.typing.lens.LensTypeCategoryName

data class LensTypeCategory(
    val id: String = "",
    val name: String = "",
    val explanation: String = "",
) {
    val category = LensTypeCategoryName.fromName(name)
    val isMono = category != LensTypeCategoryName.Multi
            && category != LensTypeCategoryName.None
}