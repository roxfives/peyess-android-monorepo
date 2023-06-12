package com.peyess.salesapp.data.model.lens.coloring


data class StoreLensColoringDocument(
    val id: String = "",
    val brand: String = "",
    val cost: Double = 0.0,
    val price: Double = 0.0,
    val design: String = "",
    val display: List<String> = emptyList(),
    val explanations: List<String> = emptyList(),
    val hasMedical: Boolean = false,
    val isEnabled: Boolean = false,
    val isLocalEnabled: Boolean = false,
    val isTreatmentRequired: Boolean = false,
    val observation: String = "",
    val priority: Double = 0.0,
    val shippingTime: Double = 0.0,
    val specialty: String = "",
    val suggestedPrice: Double = 0.0,
    val supplierId: String = "",
    val supplier: String = "",
    val type: String = "",
    val warning: String = "",
)