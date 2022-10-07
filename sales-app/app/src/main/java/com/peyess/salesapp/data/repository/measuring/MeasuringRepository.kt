package com.peyess.salesapp.data.repository.measuring

import com.peyess.salesapp.data.model.measuring.MeasuringDocument

interface MeasuringRepository {
    suspend fun add(measuring: MeasuringDocument)
}