package com.peyess.salesapp.data.repository.local_sale.lens_comparison

import com.peyess.salesapp.data.adapter.local_sale.lens_comparison.toLensComparisonDocument
import com.peyess.salesapp.data.adapter.local_sale.lens_comparison.toLensComparisonEntity
import com.peyess.salesapp.data.dao.local_sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import javax.inject.Inject

class LensComparisonRepositoryImpl @Inject constructor(
    private val lensComparisonDao: LensComparisonDao,
) : LensComparisonRepository {

    override suspend fun getBySo(soId: String): List<LensComparisonDocument> {
        return lensComparisonDao
            .getBySo(soId)
            .map { it.toLensComparisonDocument() }
    }

    override suspend fun deleteById(comparisonId: Int) {
        lensComparisonDao.deleteById(comparisonId)
    }

    override suspend fun deleteAllFromSO(soId: String) {
        lensComparisonDao.deleteAllFromSO(soId)
    }

    override suspend fun add(lensComparisonDocument: LensComparisonDocument) {
        lensComparisonDao.add(lensComparisonDocument.toLensComparisonEntity())
    }

    override suspend fun update(activeSale: LensComparisonDocument) {
        lensComparisonDao.update(activeSale.toLensComparisonEntity())
    }
}
