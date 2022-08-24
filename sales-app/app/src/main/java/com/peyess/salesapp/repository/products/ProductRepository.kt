package com.peyess.salesapp.repository.products

import androidx.paging.PagingData
import com.peyess.salesapp.dao.products.firestore.lens_description.LensDescription
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.model.products.LensGroup
import com.peyess.salesapp.model.products.LensTypeCategory
import kotlinx.coroutines.flow.Flow

data class LensFilter(
    val groupId: String = "",
    val lensTypeId: String = "",
    val supplierId: String = "",
    val materialId: String = "",
    val familyId: String = "",
    val descriptionId: String = "",
)

interface ProductRepository {
    fun filteredLenses(lensFilter: LensFilter): Flow<PagingData<LensSuggestionModel>>

    fun lensGroups(): Flow<List<LensGroup>>
    fun lensTypes(): Flow<List<FilterLensTypeEntity>>
    fun lensSuppliers(): Flow<List<FilterLensSupplierEntity>>
    fun lensMaterial(supplierId: String): Flow<List<FilterLensMaterialEntity>>
    fun lensFamily(supplierId: String): Flow<List<FilterLensFamilyEntity>>
    fun lensDescription(familyId: String): Flow<List<LensDescription>>
    fun bestLensInGroup(groupId: String): Flow<LensSuggestionModel?>
}