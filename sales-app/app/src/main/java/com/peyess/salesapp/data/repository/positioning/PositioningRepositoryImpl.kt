package com.peyess.salesapp.data.repository.positioning

import com.peyess.salesapp.data.adapter.positioning.toFSPositioning
import com.peyess.salesapp.data.dao.positioning.PositioningDao
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import javax.inject.Inject

class PositioningRepositoryImpl @Inject constructor(
    private val positioningDao: PositioningDao,
): PositioningRepository {
    override suspend fun add(positioning: PositioningDocument) {
        val fsPositioning = positioning.toFSPositioning()

        positioningDao.add(fsPositioning)
    }
}