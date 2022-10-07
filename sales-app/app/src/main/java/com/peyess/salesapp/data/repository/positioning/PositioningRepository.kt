package com.peyess.salesapp.data.repository.positioning

import com.peyess.salesapp.data.model.positioning.PositioningDocument

interface PositioningRepository {
    suspend fun add(positioning: PositioningDocument)
}