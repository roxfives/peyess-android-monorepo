package com.peyess.salesapp.data.repository.positioning

import arrow.core.continuations.either
import com.peyess.salesapp.data.adapter.positioning.toFSPositioning
import com.peyess.salesapp.data.dao.positioning.PositioningDao
import com.peyess.salesapp.data.dao.positioning.error.ReadPositioningDaoError
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.repository.positioning.adapter.toPositioningDocument
import com.peyess.salesapp.data.repository.positioning.error.ReadPositioningRepositoryError
import javax.inject.Inject

class PositioningRepositoryImpl @Inject constructor(
    private val positioningDao: PositioningDao,
): PositioningRepository {
    override suspend fun add(positioning: PositioningDocument) {
        val fsPositioning = positioning.toFSPositioning()

        positioningDao.add(fsPositioning)
    }

    override suspend fun positioningById(
        positioningId: String,
    ): ReadPositioningResponse = either {
        positioningDao
            .positioningById(positioningId)
            .mapLeft {
                when(it) {
                    is ReadPositioningDaoError.NotFound ->
                        ReadPositioningRepositoryError.NotFound(
                            description = it.description,
                            throwable = it.throwable,
                        )
                    is ReadPositioningDaoError.Unexpected ->
                        ReadPositioningRepositoryError.Unexpected(
                            description = it.description,
                            throwable = it.throwable,
                        )
                }
            }.bind()
            .toPositioningDocument()
    }
}
