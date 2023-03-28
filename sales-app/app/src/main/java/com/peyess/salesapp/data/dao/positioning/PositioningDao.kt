package com.peyess.salesapp.data.dao.positioning

import arrow.core.Either
import com.peyess.salesapp.data.dao.positioning.error.ReadPositioningDaoError
import com.peyess.salesapp.data.dao.positioning.error.UpdatePositioningDaoError
import com.peyess.salesapp.data.model.positioning.FSPositioning
import com.peyess.salesapp.data.model.positioning.FSPositioningUpdate

typealias ReadPositioningResponse = Either<ReadPositioningDaoError, FSPositioning>

typealias UpdatePositioningResponse = Either<UpdatePositioningDaoError, Unit>

interface PositioningDao {
    suspend fun add(document: FSPositioning)

    suspend fun positioningById(positioningId: String): ReadPositioningResponse

    suspend fun updatePositioning(
        positioningId: String,
        positioningUpdate: FSPositioningUpdate,
    ): UpdatePositioningResponse
}
