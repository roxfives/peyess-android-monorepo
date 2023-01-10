package com.peyess.salesapp.data.repository.local_sale.lens_comparison

import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument

interface LensComparisonRepository {
    suspend fun getBySo(soId: String): List<LensComparisonDocument>
    suspend fun deleteById(comparisonId: Int)
    suspend fun deleteAllFromSO(soId: String)
    suspend fun add(lensComparisonDocument: LensComparisonDocument)
    suspend fun update(activeSale: LensComparisonDocument)
}