package com.peyess.salesapp.repository.products

import androidx.paging.PagingData
import com.peyess.salesapp.data.model.lens.description.LensDescriptionDocument
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.data.model.lens.groups.LensGroupDocument
import kotlinx.coroutines.flow.Flow

data class LensFilter(
    val groupId: String = "",
    val specialtyId: String = "",
    val lensTypeId: String = "",
    val supplierId: String = "",
    val materialId: String = "",
    val familyId: String = "",
    val descriptionId: String = "",

    val hasFilterUv: Boolean = false,
    val hasFilterBlue: Boolean = false,
)

interface ProductRepository {
    fun filteredLenses(lensFilter: LensFilter): Flow<PagingData<LensSuggestionModel>>

    fun lensGroups(): Flow<List<LensGroupDocument>>
    fun lensSpecialties(): Flow<List<FilterLensSpecialtyEntity>>
    fun lensTypes(): Flow<List<FilterLensTypeEntity>>
    fun lensSuppliers(): Flow<List<FilterLensSupplierEntity>>
    fun lensMaterial(supplierId: String): Flow<List<FilterLensMaterialEntity>>
    fun lensFamily(supplierId: String): Flow<List<FilterLensFamilyEntity>>
    fun lensDescription(familyId: String): Flow<List<LensDescriptionDocument>>
    fun bestLensInGroup(groupId: String): Flow<LensSuggestionModel?>

    fun lensById(lensId: String): Flow<LocalLensEntity?>
    fun treatmentById(treatmentId: String): Flow<LocalTreatmentEntity?>
    fun coloringById(coloringId: String): Flow<LocalColoringEntity?>
    fun treatmentsForLens(lensId: String): Flow<List<LocalTreatmentEntity>>
    fun coloringsForLens(lensId: String): Flow<List<LocalColoringEntity>>

    fun techsForLens(supplierId: String, brandId: String, designId: String):
            Flow<List<FilterLensTechEntity>>
    fun materialsForLens(supplierId: String, brandId: String, designId: String):
            Flow<List<FilterLensMaterialEntity>>

    fun lensWith(
        supplierId: String,
        brandId: String,
        designId: String,
        techId: String,
        materialId: String
    ): Flow<LocalLensEntity?>
}