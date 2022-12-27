package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDetailsCrossRef

private const val lensTypeTable = LocalLensTypeEntity.tableName
private const val lensesDetailsTable = LocalLensDetailsCrossRef.tableName

@DatabaseView(
    viewName = LocalLensesTypeDBView.viewName,
    value = """
        SELECT
            lensesTypes.name AS id,
            lensesTypes.name AS name,
            lensesDetails.brand_id AS lensFamilyId,
            lensesDetails.design_id AS lensDescriptionId,
            lensesDetails.supplier_id AS lensSupplierId,
            lensesDetails.group_id AS lensGroupId,
            lensesDetails.specialty_id AS lensSpecialtyId,
            lensesDetails.tech_id AS lensTechId,
            lensesDetails.type_id AS lensTypeId,
            lensesDetails.category_id AS lensCategoryId,
            lensesDetails.material_id AS lensMaterialId
         FROM $lensTypeTable AS lensesTypes
         JOIN $lensesDetailsTable AS lensesDetails ON lensesTypes.id = lensesDetails.type_id
    """
)
data class LocalLensesTypeDBView(
    val id: String = "",
    val name: String = "",

    val lensFamilyId: String = "",
    val lensDescriptionId: String = "",
    val lensSupplierId: String = "",
    val lensGroupId: String = "",
    val lensSpecialtyId: String = "",
    val lensTechId: String = "",
    val lensTypeId: String = "",
    val lensCategoryId: String = "",
    val lensMaterialId: String = "",
) {
    companion object {
        const val viewName = "local_lenses_type_db_view"
    }
}
