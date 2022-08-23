package com.peyess.salesapp.repository.products

import androidx.paging.PagingData
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
    fun lensMaterialDao(supplierId: String): Flow<List<FilterLensMaterialEntity>>
}