package com.peyess.salesapp.data.model.lens.room.dao.database_view

import androidx.room.DatabaseView
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDetailsCrossRef

private const val lensDescriptionsTable = LocalLensDescriptionEntity.tableName
private const val lensesDetailsTable = LocalLensDetailsCrossRef.tableName

@DatabaseView(
    viewName = LocalLensesDescriptionDBView.viewName,
    value = """
        SELECT
            lensesDescriptions.id AS id,
            lensesDescriptions.name AS name,
            lensesDetails.brand_id AS lensFamilyId,
            lensesDetails.design_id AS lensDescriptionId,
            lensesDetails.supplier_id AS lensSupplierId,
            lensesDetails.group_id AS lensGroupId,
            lensesDetails.specialty_id AS lensSpecialtyId,
            lensesDetails.tech_id AS lensTechId,
            lensesDetails.type_id AS lensTypeId,
            lensesDetails.category_id AS lensCategoryId,
            lensesDetails.material_id AS lensMaterialId
         FROM $lensDescriptionsTable AS lensesDescriptions
         JOIN $lensesDetailsTable AS lensesDetails ON lensesDescriptions.id = lensesDetails.design_id
    """
)
data class LocalLensesDescriptionDBView(
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
        const val viewName = "local_lenses_description_db_view"
    }
}
