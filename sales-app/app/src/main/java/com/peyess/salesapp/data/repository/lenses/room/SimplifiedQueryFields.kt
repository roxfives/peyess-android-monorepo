package com.peyess.salesapp.data.repository.lenses.room

sealed class SimplifiedQueryFields {
    object LensType: SimplifiedQueryFields()
    object LensSupplier: SimplifiedQueryFields()
    object LensFamily: SimplifiedQueryFields()
    object LensDescription: SimplifiedQueryFields()
    object LensMaterial: SimplifiedQueryFields()
    object LensSpecialty: SimplifiedQueryFields()
    object LensGroup: SimplifiedQueryFields()

    object Priority: SimplifiedQueryFields()
    object Name: SimplifiedQueryFields()

    fun name() = name(this)

    companion object {
        fun name (field: SimplifiedQueryFields): String {
            return when (field) {
                is LensType -> "lensTypeId"
                is LensSupplier -> "lensSupplierId"
                is LensFamily -> "lensFamilyId"
                is LensDescription -> "lensDescriptionId"
                is LensMaterial -> "lensMaterialId"
                is LensSpecialty -> "lensSpecialtyId"
                is LensGroup -> "lensGroupId"
                is Name -> "name"
                is Priority -> "priority"
            }
        }
    }
}