package com.peyess.salesapp.data.model.lens.room.repo

data class StoreLensDisponibilityWithoutManufacturersDocument(
    val diam: Double = 0.0,

    val maxCyl: Double = 0.0,

    val minCyl: Double = 0.0,

    val maxSph: Double = 0.0,

    val minSph: Double = 0.0,

    val maxAdd: Double = 0.0,

    val minAdd: Double = 0.0,

    val hasPrism: Boolean = false,

    val prism: Double = 0.0,

    val prismPrice: Double = 0.0,

    val prismCost: Double = 0.0,

    val separatePrism: Boolean = false,

    val needsCheck: Boolean = false,

    val sumRule: Boolean = false,
)
