package com.peyess.salesapp.dao.products.firestore.lens_description

data class LensDescription(
    val id: String = "",
    val name: String = "",
    val priority: Double = 0.0,
    val familiesIds: List<String> = emptyList(),
)
