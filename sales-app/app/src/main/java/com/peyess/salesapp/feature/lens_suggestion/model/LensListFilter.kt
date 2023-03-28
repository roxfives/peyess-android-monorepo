package com.peyess.salesapp.feature.lens_suggestion.model

data class LensListFilter(
    val lensTypeId: String = "",
    val supplierId: String = "",
    val familyId: String = "",
    val descriptionId: String = "",
    val materialId: String = "",
    val specialtyId: String = "",
    val groupId: String = "",

    val withFilterUv: Boolean = false,
    val withFilterBlue: Boolean = false,
)
