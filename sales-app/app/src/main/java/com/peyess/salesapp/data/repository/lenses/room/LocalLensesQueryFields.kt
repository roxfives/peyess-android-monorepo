package com.peyess.salesapp.data.repository.lenses.room

sealed class LocalLensesQueryFields {
    object LensTypePriority: LocalLensesQueryFields()
    object LensType: LocalLensesQueryFields()
    object LensTypeId: LocalLensesQueryFields()

    object LensSupplierPriority: LocalLensesQueryFields()
    object LensSupplier: LocalLensesQueryFields()
    object LensSupplierId: LocalLensesQueryFields()

    object LensFamilyPriority: LocalLensesQueryFields()
    object LensFamily: LocalLensesQueryFields()
    object LensFamilyId: LocalLensesQueryFields()

    object LensDescriptionPriority: LocalLensesQueryFields()
    object LensDescription: LocalLensesQueryFields()
    object LensDescriptionId: LocalLensesQueryFields()

    object LensMaterialPriority: LocalLensesQueryFields()
    object LensMaterial: LocalLensesQueryFields()
    object LensMaterialId: LocalLensesQueryFields()

    object LensSpecialtyPriority: LocalLensesQueryFields()
    object LensSpecialty: LocalLensesQueryFields()
    object LensSpecialtyId: LocalLensesQueryFields()

    object LensGroupPriority: LocalLensesQueryFields()
    object LensGroup: LocalLensesQueryFields()
    object LensGroupId: LocalLensesQueryFields()

    object LensUVLightFilter: LocalLensesQueryFields()
    object LensBlueLightFilter: LocalLensesQueryFields()

//    val isTypeMono: String = "isLensTypeMono",
//
//    val maxSpherical: String = "maxSpherical",
//    val minSpherical: String = "minSpherical",
//
//    val maxCylindrical: String = "maxCylindrical",
//    val minCylindrical: String = "minCylindrical",
//
//    val maxAddition: String = "maxAddition",
//    val minAddition: String = "minAddition",
//
//    val diameter: String = "diameter",
//    val prism: String = "prism",
//    val height: String = "heightResult",

    fun name(): String {
        return when (this) {
            is LensType -> "typeName"
            is LensTypePriority -> "typePriority"
            is LensTypeId -> "typeId"
            is LensSupplier -> "supplierName"
            is LensSupplierPriority -> "supplierPriority"
            is LensSupplierId -> "supplierId"
            is LensFamily -> "brandName"
            is LensFamilyPriority -> "brandPriority"
            is LensFamilyId -> "brandId"
            is LensDescription -> "designName"
            is LensDescriptionPriority -> "designPriority"
            is LensDescriptionId -> "designId"
            is LensMaterial -> "materialName"
            is LensMaterialPriority -> "materialPriority"
            is LensMaterialId -> "materialId"
            is LensSpecialty -> "specialtyName"
            is LensSpecialtyPriority -> "specialtyPriority"
            is LensSpecialtyId -> "specialtyId"
            is LensGroup -> "groupName"
            is LensGroupPriority -> "groupPriority"
            is LensGroupId -> "groupId"
            is LensBlueLightFilter -> "hasFilterBlue"
            is LensUVLightFilter -> "hasFilterUv"
        }
    }
}
