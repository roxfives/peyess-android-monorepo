package com.peyess.salesapp.feature.sale.lens_comparison.model

data class Coloring(
    val id: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val design: String = "",
    val hasMedical: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val isTreatmentRequired: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val supplier: String = "",
    val type: String = "",
    val warning: String = "",
    val explanations: List<String> = emptyList(),
) {
    val name = "$brand $design"
}
