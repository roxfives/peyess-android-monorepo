package com.peyess.salesapp.features.disponibility.model

data class Disponibility(
    val diameter: Double = 0.0,
    val height: Double = 0.0,
    val isLensTypeMono: Boolean = false,

    val maxCylindrical: Double = 0.0,
    val minCylindrical: Double = 0.0,
    val maxSpherical: Double = 0.0,
    val minSpherical: Double = 0.0,
    val maxAddition: Double = 0.0,
    val minAddition: Double = 0.0,

    val hasPrism: Boolean = false,
    val prism: Double = 0.0,

    val sumRule: Boolean = false,

    val hasAlternativeHeight: Boolean = false,
    val alternativeHeights: List<AlternativeHeight> = emptyList(),
)
