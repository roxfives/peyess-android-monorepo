package com.peyess.salesapp.feature.service_order.model

import java.math.BigDecimal

data class Lens(
    val id: String = "",

    val hasAddition: Boolean = false,
    val hasFilterBlue: Boolean = false,
    val hasFilterUv: Boolean = false,
    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,
    val needsCheck: Boolean = false,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val price: BigDecimal = BigDecimal.ZERO,
    val priceAddColoring: BigDecimal = BigDecimal.ZERO,
    val priceAddTreatment: BigDecimal = BigDecimal.ZERO,

    val brandName: String = "",
    val designName: String = "",
    val supplierName: String = "",
    val groupName: String = "",
    val specialtyName: String = "",
    val techName: String = "",
    val typeName: String = "",
    val categoryName: String = "",
    val materialName: String = "",

    val explanations: List<String> = emptyList(),
) {
    val name = "$brandName $designName $techName $materialName"
}
