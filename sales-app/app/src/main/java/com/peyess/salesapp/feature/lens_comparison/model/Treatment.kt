package com.peyess.salesapp.feature.lens_comparison.model

data class Treatment(
    val id: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val design: String = "",
    val isColoringRequired: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
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