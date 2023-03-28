package com.peyess.salesapp.data.repository.local_sale.lens_comparison

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument

typealias InsertResponse = Either<LocalComparisonInsertError, Unit>
typealias UpdateResponse = Either<LocalComparisonUpdateError, Unit>
typealias DeleteResponse = Either<LocalComparisonDeletionError, Unit>
typealias FetchListResponse = Either<LocalComparisonReadError, List<LensComparisonDocument>>

interface LensComparisonRepository {
    suspend fun getBySo(soId: String): FetchListResponse
    suspend fun deleteById(comparisonId: Int): DeleteResponse
    suspend fun deleteAllFromSO(soId: String): DeleteResponse
    suspend fun add(lensComparisonDocument: LensComparisonDocument): InsertResponse
    suspend fun update(lensComparison: LensComparisonDocument): UpdateResponse
}
