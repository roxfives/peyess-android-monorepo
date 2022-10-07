package com.peyess.salesapp.data.dao.positioning

import com.peyess.salesapp.data.model.positioning.FSPositioning

interface PositioningDao {
    suspend fun add(document: FSPositioning)
}