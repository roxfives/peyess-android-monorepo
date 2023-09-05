package com.peyess.salesapp.feature.service_order.model

import java.math.BigDecimal

data class Treatment(
    val id: String = "",
    val brand: String = "",
    val price: BigDecimal = BigDecimal.ZERO,
    val isColoringRequired: Boolean = false,
    val design: String = "",
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val supplier: String = "",
    val warning: String = "",
    val explanations: List<String> = emptyList(),
) {
    val name = "$brand $design"
}