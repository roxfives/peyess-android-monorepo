package com.peyess.salesapp.feature.sale.lens_pick.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.peyess.salesapp.features.disponibility.contants.ReasonUnsupported
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

    val explanations: List<String> = emptyList(),
    val observation: String = "",
    val warning: String = "",

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,

    val needsCheck: Boolean = false,

    val reasonsUnavailable: List<String> = emptyList(),
) {
    val name = "$family $description $tech $material"

    val isAvailable = reasonsUnavailable.isEmpty()
    val isNotAvailable = !isAvailable
}
