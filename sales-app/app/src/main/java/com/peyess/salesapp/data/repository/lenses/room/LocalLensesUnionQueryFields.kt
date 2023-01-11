package com.peyess.salesapp.data.repository.lenses.room

sealed class LocalLensesUnionQueryFields {
    object LensTechId: LocalLensesUnionQueryFields()
    object LensTechPriority: LocalLensesUnionQueryFields()

    object LensId: LocalLensesUnionQueryFields()
    object LensPriority: LocalLensesUnionQueryFields()

    object LensTypePriority: LocalLensesUnionQueryFields()
    object LensType: LocalLensesUnionQueryFields()
    object LensTypeId: LocalLensesUnionQueryFields()
    object IsLensTypeMono: LocalLensesUnionQueryFields()

    object LensSupplierPriority: LocalLensesUnionQueryFields()
    object LensSupplier: LocalLensesUnionQueryFields()
    object LensSupplierId: LocalLensesUnionQueryFields()

    object LensFamilyPriority: LocalLensesUnionQueryFields()
    object LensFamily: LocalLensesUnionQueryFields()
    object LensFamilyId: LocalLensesUnionQueryFields()

    object LensDescriptionPriority: LocalLensesUnionQueryFields()
    object LensDescription: LocalLensesUnionQueryFields()
    object LensDescriptionId: LocalLensesUnionQueryFields()

    object LensMaterialPriority: LocalLensesUnionQueryFields()
    object LensMaterial: LocalLensesUnionQueryFields()
    object LensMaterialId: LocalLensesUnionQueryFields()

    object LensSpecialtyPriority: LocalLensesUnionQueryFields()
    object LensSpecialty: LocalLensesUnionQueryFields()
    object LensSpecialtyId: LocalLensesUnionQueryFields()

    object LensGroupPriority: LocalLensesUnionQueryFields()
    object LensGroup: LocalLensesUnionQueryFields()
    object LensGroupId: LocalLensesUnionQueryFields()

    object LensUVLightFilter: LocalLensesUnionQueryFields()
    object LensBlueLightFilter: LocalLensesUnionQueryFields()

    object MaxSpherical: LocalLensesUnionQueryFields()
    object MinSpherical: LocalLensesUnionQueryFields()

    object MaxCylindrical: LocalLensesUnionQueryFields()
    object MinCylindrical: LocalLensesUnionQueryFields()

    object MaxAddition: LocalLensesUnionQueryFields()
    object MinAddition: LocalLensesUnionQueryFields()

    object HasSumRule: LocalLensesUnionQueryFields()

    object Diameter: LocalLensesUnionQueryFields()
    object Prism: LocalLensesUnionQueryFields()
    object Height: LocalLensesUnionQueryFields()

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
            is Diameter -> "diameter"
            is Height -> "height"
            is IsLensTypeMono -> "isLensMono"
            is MaxAddition -> "maxAddition"
            is MaxCylindrical -> "maxCylindrical"
            is MaxSpherical -> "maxSpherical"
            is MinAddition -> "minAddition"
            is MinCylindrical -> "minCylindrical"
            is MinSpherical -> "minSpherical"
            is Prism -> "prism"
            is LensPriority -> "priority"
            is LensId -> "id"
            is HasSumRule -> "sumRule"
            is LensTechId -> "techId"
            is LensTechPriority -> "techName"
        }
    }
}
