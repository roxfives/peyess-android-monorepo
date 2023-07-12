package com.peyess.salesapp.repository.stats

import arrow.core.Either
import com.peyess.salesapp.data.model.stats.LensStatsDocument
import com.peyess.salesapp.repository.stats.error.FetchProductStatsError

typealias LensStatsResponse = Either<FetchProductStatsError, LensStatsDocument>

interface ProductStatsRepository {
    suspend fun fetchLensStats(): LensStatsResponse
}