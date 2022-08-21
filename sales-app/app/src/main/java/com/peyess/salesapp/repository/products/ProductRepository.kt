package com.peyess.salesapp.repository.products

import androidx.paging.PagingData
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import kotlinx.coroutines.flow.Flow

data class LensFilter(
    val supplierId: String = ""
)

interface ProductRepository {
    fun filteredLenses(
        lensFilter: LensFilter,
        // prescriptionData
        // measuring (fixed by model)
    ): Flow<PagingData<LensSuggestionModel>>
}