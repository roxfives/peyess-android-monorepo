package com.peyess.salesapp.feature.sale.lens_pick.model

import java.math.BigDecimal

data class LensPickModel(
    val id: String = "",

    val family: String = "",
    val description: String = "",
    val tech: String = "",
    val material: String = "",
    val specialty: String = "",
    val category: String = "",

    val supplier: String = "",

    val price: BigDecimal = BigDecimal(0.0),

    val explanations: String = "",
    val observation: String = "",
    val warning: String = "",

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,

    val needsCheck: Boolean = false,

    val isAvailable: Boolean = false,
    val reasonUnavailable: String = "",
) {
    val name = "$family $description $tech $material"
}
