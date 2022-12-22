package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import java.time.ZonedDateTime

private const val lensesTable = LocalLensEntity.tableName
private const val brandsTable = LocalLensFamilyEntity.tableName
private const val designTable = LocalLensDescriptionEntity.tableName
private const val supplierTable = LocalLensSupplierEntity.tableName
private const val groupTable = LocalLensGroupEntity.tableName
private const val specialtyTable = LocalLensSpecialtyEntity.tableName
private const val techTable = LocalLensTechEntity.tableName
private const val typeTable = LocalLensTypeEntity.tableName
private const val categoryTable = LocalLensCategoryEntity.tableName
private const val materialTable = LocalLensMaterialEntity.tableName

@DatabaseView(
    viewName = LocalLensWithDetailsDBView.viewName,
    value = """
        SELECT 
            lenses.id AS id,
            lenses.priority AS priority,
            lenses.store_lens_priority AS storeLensPriority,
            lenses.height AS height,
            lenses.has_addition AS hasAddition,
            lenses.has_filter_blue AS hasFilterBlue,
            lenses.has_filter_uv AS hasFilterUv,
            lenses.is_coloring_treatment_mutex AS isColoringTreatmentMutex,
            lenses.is_coloring_discounted AS isColoringDiscounted,
            lenses.is_coloring_included AS isColoringIncluded,
            lenses.is_treatment_discounted AS isTreatmentDiscounted,
            lenses.is_treatment_included AS isTreatmentIncluded,
            lenses.is_generic AS isGeneric,
            lenses.shipping_time AS shippingTime,
            lenses.observation AS observation,
            lenses.warning AS warning,
            lenses.is_manufacturing_local AS isManufacturingLocal,
            lenses.is_enabled AS isEnabled,
            lenses.reason_disabled AS reasonDisabled,
            lenses.is_local_enabled AS isLocalEnabled,
            lenses.reason_local_disabled AS reasonLocalDisabled,
            lenses.price AS price,
            lenses.price_add_coloring AS priceAddColoring,
            lenses.price_add_treatment AS priceAddTreatment,
            lenses.is_editable AS isEditable,
            lenses.default_treatment_id AS defaultTreatmentId,
            lenses.created AS created,
            lenses.updated AS updated,
            
            lenses.brand_id AS brandId,
            lenses.design_id AS designId,
            lenses.supplier_id AS supplierId,
            lenses.group_id AS groupId,
            lenses.specialty_id AS specialtyId,
            lenses.tech_id AS techId,
            lenses.type_id AS typeId,
            lenses.category_id AS categoryId,
            lenses.material_id AS materialId,
            
            brands.name AS brandName,
            brands.priority AS brandPriority,
            designs.name AS designName,
            designs.priority AS designPriority,
            suppliers.name AS supplierName,
            suppliers.priority AS supplierPriority,
            groups.name AS groupName,
            groups.priority AS groupPriority,
            specialties.name AS specialtyName,
            specialties.priority AS specialtyPriority,
            techs.name AS techName,
            techs.priority AS techPriority,
            types.name AS typeName,
            types.priority AS typePriority,
            categories.name AS categoryName,
            categories.priority AS categoryPriority,
            materials.name AS materialName,
            materials.priority AS materialPriority
            
        FROM $lensesTable AS lenses
        JOIN $brandsTable AS brands ON lenses.brand_id = brands.id
        JOIN $designTable AS designs ON lenses.design_id = designs.id
        JOIN $supplierTable AS suppliers ON lenses.supplier_id = suppliers.id
        JOIN $groupTable AS groups ON lenses.group_id = groups.id
        JOIN $specialtyTable AS specialties ON lenses.specialty_id = specialties.id
        JOIN $techTable AS techs ON lenses.tech_id = techs.id
        JOIN $typeTable AS types ON lenses.type_id = types.id
        JOIN $categoryTable AS categories ON lenses.category_id = categories.id
        JOIN $materialTable AS materials ON lenses.material_id = materials.id
    """
)
data class LocalLensWithDetailsDBView(
    val id: String = "",

    val priority: Double = 0.0,
    val storeLensPriority: Int = 0,

    val height: Double = 0.0,
    val hasAddition: Boolean = false,
    val hasFilterBlue: Boolean = false,
    val hasFilterUv: Boolean = false,
    val isColoringTreatmentMutex: Boolean = false,
    val isColoringDiscounted: Boolean = false,
    val isColoringIncluded: Boolean = false,
    val isTreatmentDiscounted: Boolean = false,
    val isTreatmentIncluded: Boolean = false,
    val isGeneric: Boolean = false,

    val shippingTime: Double = 0.0,

    val observation: String = "",
    val warning: String = "",

    val isManufacturingLocal: Boolean = false,
    val isEnabled: Boolean = false,
    val reasonDisabled: String = "",
    val isLocalEnabled: Boolean = true,
    val reasonLocalDisabled: String = "",

    val price: Double = 0.0,
    val priceAddColoring: Double = 0.0,
    val priceAddTreatment: Double = 0.0,

    val isEditable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val updated: ZonedDateTime = ZonedDateTime.now(),

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

    val brandPriority: String = "",
    val designPriority: String = "",
    val supplierPriority: String = "",
    val groupPriority: String = "",
    val specialtyPriority: String = "",
    val techPriority: String = "",
    val typePriority: String = "",
    val categoryPriority: String = "",
    val materialPriority: String = "",
) {
    companion object {
        const val viewName = "lenses_with_details"
    }
}