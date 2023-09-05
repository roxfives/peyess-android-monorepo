package com.peyess.salesapp.repository.stats

import com.peyess.salesapp.dao.stats.LensStatsDao
import com.peyess.salesapp.dao.stats.error.FetchLensStatsDaoError
import com.peyess.salesapp.data.adapter.stats.toProductStatsDocument
import com.peyess.salesapp.repository.stats.error.FetchProductStatsError
import javax.inject.Inject

class ProductStatsRepositoryImpl @Inject constructor(
    private val lensStatsDao: LensStatsDao,
): ProductStatsRepository {
    override suspend fun fetchLensStats(): LensStatsResponse {
        return lensStatsDao.fetchLensStats().mapLeft {
            when (it) {
                is FetchLensStatsDaoError.DatabaseUnavailable ->
                    FetchProductStatsError.DatabaseUnavailable(it.description, it.throwable)
                is FetchLensStatsDaoError.NotFound ->
                    FetchProductStatsError.NotFound(it.description, it.throwable)
                is FetchLensStatsDaoError.Unexpected ->
                    FetchProductStatsError.Unexpected(it.description, it.throwable)
            }
        }.map {
            it.toProductStatsDocument()
        }
    }
}
