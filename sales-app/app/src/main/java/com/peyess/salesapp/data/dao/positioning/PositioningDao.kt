package com.peyess.salesapp.data.dao.positioning

import arrow.core.Either
import com.peyess.salesapp.data.dao.positioning.error.ReadPositioningDaoError
import com.peyess.salesapp.data.model.positioning.FSPositioning

typealias ReadPositioningResponse = Either<ReadPositioningDaoError, FSPositioning>

interface PositioningDao {
    suspend fun add(document: FSPositioning)

    suspend fun positioningById(positioningId: String): ReadPositioningResponse
}
