package com.peyess.salesapp.data.repository.local_sale.lens_comparison

import arrow.core.Either
import com.peyess.salesapp.data.adapter.local_sale.lens_comparison.toLensComparisonDocument
import com.peyess.salesapp.data.adapter.local_sale.lens_comparison.toLensComparisonEntity
import com.peyess.salesapp.data.dao.local_sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import javax.inject.Inject

class LensComparisonRepositoryImpl @Inject constructor(
    private val lensComparisonDao: LensComparisonDao,
) : LensComparisonRepository {

    override suspend fun getBySo(soId: String): FetchListResponse = Either.catch {
        lensComparisonDao
            .getBySo(soId)
            .map { it.toLensComparisonDocument() }
    }.mapLeft {
        FetchError(
            description = "Error fetching lens comparison list: ${it.message}",
            error = it
        )
    }

    override suspend fun deleteById(comparisonId: Int): DeleteResponse = Either.catch {
        lensComparisonDao.deleteById(comparisonId)
    }.mapLeft {
        DeleteError(
            description = "Error deleting lens comparison: ${it.message}",
            error = it
        )
    }

    override suspend fun deleteAllFromSO(soId: String): DeleteResponse = Either.catch {
        lensComparisonDao.deleteAllFromSO(soId)
    }.mapLeft {
        DeleteError(
            description = "Error deleting all lens comparisons for SO $soId: ${it.message}",
            error = it
        )
    }

    override suspend fun add(
        lensComparisonDocument: LensComparisonDocument
    ): InsertResponse = Either.catch {
        lensComparisonDao.add(lensComparisonDocument.toLensComparisonEntity())
    }.mapLeft { error ->
        CreateError(
            description = "Error creating lens comparison: ${error.message}",
            error = error
        )
    }

    override suspend fun update(lensComparison: LensComparisonDocument) = Either.catch {
        lensComparisonDao.update(lensComparison.toLensComparisonEntity())
    }.mapLeft {
        UpdateError(
            description = "Error updating lens comparison: ${it.message}",
            error = it
        )
    }
}
