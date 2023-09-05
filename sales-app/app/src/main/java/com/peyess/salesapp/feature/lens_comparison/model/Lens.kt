package com.peyess.salesapp.feature.lens_comparison.model

import java.math.BigDecimal

data class Lens(
    val id: String = "",

    val priority: Double = 0.0,
    val storeLensPriority: Int = 0,

    val hasFilterBlue: Boolean = false,
    val hasFilterUv: Boolean = false,

    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,

    val isGeneric: Boolean = false,
    val needsCheck: Boolean = false,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val price: BigDecimal = BigDecimal.ZERO,
    val priceAddColoring: BigDecimal = BigDecimal.ZERO,
    val priceAddTreatment: BigDecimal = BigDecimal.ZERO,

    val brandId: String = "",
    val designId: String = "",
    val supplierId: String = "",
    val groupId: String = "",
    val specialtyId: String = "",
    val techId: String = "",
    val typeId: String = "",
    val categoryId: String = "",
    val materialId: String = "",
    val defaultTreatmentId: String = "",

    val brandName: String = "",
    val designName: String = "",
    val supplierName: String = "",
    val groupName: String = "",
    val specialtyName: String = "",
    val techName: String = "",
    val typeName: String = "",
    val categoryName: String = "",
    val materialName: String = "",

    val materialCategory: String = "",

    val explanations: List<String> = emptyList(),
)
