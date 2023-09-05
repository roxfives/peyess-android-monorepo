package com.peyess.salesapp.dao.stats

import arrow.core.Either
import com.peyess.salesapp.dao.stats.error.FetchLensStatsDaoError
import com.peyess.salesapp.data.model.stats.FSLensStats

typealias LensStatsResponse = Either<FetchLensStatsDaoError, FSLensStats>

interface LensStatsDao {
    suspend fun fetchLensStats(): LensStatsResponse
}