package com.peyess.salesapp.data.dao.measuring

import arrow.core.Either
import com.peyess.salesapp.data.dao.measuring.error.ReadMeasuringDaoError
import com.peyess.salesapp.data.dao.measuring.error.UpdateMeasuringDaoError
import com.peyess.salesapp.data.model.measuring.FSMeasuring
import com.peyess.salesapp.data.model.measuring.FSMeasuringUpdate

typealias ReadMeasuringDaoResponse = Either<ReadMeasuringDaoError, FSMeasuring>

typealias UpdateMeasuringDaoResponse = Either<UpdateMeasuringDaoError, Unit>

interface MeasuringDao {
    suspend fun add(document: FSMeasuring)

    suspend fun measuringById(measuringId: String): ReadMeasuringDaoResponse

    suspend fun updateMeasuring(
        measuringId: String,
        measuringUpdate: FSMeasuringUpdate,
    ): UpdateMeasuringDaoResponse
}