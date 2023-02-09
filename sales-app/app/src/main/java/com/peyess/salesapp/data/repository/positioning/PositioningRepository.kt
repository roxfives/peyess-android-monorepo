package com.peyess.salesapp.data.repository.positioning

import arrow.core.Either
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.repository.positioning.error.ReadPositioningRepositoryError

typealias ReadPositioningResponse = Either<ReadPositioningRepositoryError, PositioningDocument>

interface PositioningRepository {
    suspend fun add(positioning: PositioningDocument)

    suspend fun positioningById(positioningId: String): ReadPositioningResponse
}
