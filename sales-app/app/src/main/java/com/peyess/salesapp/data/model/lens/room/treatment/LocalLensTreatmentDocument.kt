package com.peyess.salesapp.data.model.lens.room.treatment

import java.math.BigDecimal

data class LocalLensTreatmentDocument(
    val id: String = "",
    val brand: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val design: String = "",
    val isColoringRequired: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val supplierId: String = "",
    val supplier: String = "",
    val warning: String = "",
    val explanations: List<String> = emptyList(),
) {
    val name = "$brand $design"
}
