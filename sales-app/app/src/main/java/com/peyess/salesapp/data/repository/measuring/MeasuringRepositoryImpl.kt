package com.peyess.salesapp.data.repository.measuring

import com.peyess.salesapp.data.adapter.measuring.toFSMeasuring
import com.peyess.salesapp.data.dao.measuring.MeasuringDao
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import javax.inject.Inject

class MeasuringRepositoryImpl @Inject constructor(
    private val measuringDao: MeasuringDao,
): MeasuringRepository {
    override suspend fun add(measuring: MeasuringDocument) {
        val fsMeasuring = measuring.toFSMeasuring()

        measuringDao.add(fsMeasuring)
    }
}