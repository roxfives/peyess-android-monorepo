package com.peyess.salesapp.data.dao.measuring

import arrow.core.Either
import com.peyess.salesapp.data.dao.measuring.error.ReadMeasuringDaoError
import com.peyess.salesapp.data.model.measuring.FSMeasuring

typealias ReadMeasuringDaoResponse = Either<ReadMeasuringDaoError, FSMeasuring>

interface MeasuringDao {
    suspend fun add(document: FSMeasuring)

    suspend fun measuringById(measuringId: String): ReadMeasuringDaoResponse
}