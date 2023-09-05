package com.peyess.salesapp.data.model.lens.treatment

import java.math.BigDecimal

data class StoreLensTreatmentDocument(
    val id: String = "",
    val brand: String = "",
    val cost: Double = 0.0,
    val price: BigDecimal = BigDecimal.ZERO,
    val design: String = "",
    val explanations: List<String> = emptyList(),
    val isColoringRequired: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val suggestedPrice: Double = 0.0,
    val supplierId: String = "",
    val supplier: String = "",
    val warning: String = "",
)
