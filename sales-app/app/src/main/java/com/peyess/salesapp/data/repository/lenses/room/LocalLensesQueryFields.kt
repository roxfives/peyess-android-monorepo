package com.peyess.salesapp.data.repository.lenses.room

sealed class LocalLensesQueryFields {
    object SupplierPriority: LocalLensesQueryFields()
    object Supplier: LocalLensesQueryFields()

    object LensTypePriority: LocalLensesQueryFields()
    object LensType: LocalLensesQueryFields()

    object LensMaterialPriority: LocalLensesQueryFields()
    object LensMaterial: LocalLensesQueryFields()

    fun name(): String {
        return when (this) {
            is LensMaterial -> "materialName"
            is LensMaterialPriority -> "materialPriority"
            is LensType -> "typeName"
            is LensTypePriority -> "typePriority"
            is Supplier -> "supplierName"
            is SupplierPriority -> "supplierPriority"
        }
    }
}
