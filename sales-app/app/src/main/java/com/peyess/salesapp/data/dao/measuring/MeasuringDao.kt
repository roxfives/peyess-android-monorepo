package com.peyess.salesapp.data.dao.measuring

import com.peyess.salesapp.data.model.measuring.FSMeasuring

interface MeasuringDao {
    suspend fun add(document: FSMeasuring)
}