package com.peyess.salesapp.data.repository.measuring

import arrow.core.continuations.either
import com.peyess.salesapp.data.adapter.measuring.toFSMeasuring
import com.peyess.salesapp.data.adapter.measuring.toFSMeasuringUpdate
import com.peyess.salesapp.data.dao.measuring.MeasuringDao
import com.peyess.salesapp.data.dao.measuring.error.ReadMeasuringDaoError
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.data.model.measuring.MeasuringUpdateDocument
import com.peyess.salesapp.data.repository.measuring.adapter.toMeasuringDocument
import com.peyess.salesapp.data.repository.measuring.error.ReadMeasuringRepositoryError
import com.peyess.salesapp.data.repository.measuring.error.UpdateMeasuringRepositoryError
import javax.inject.Inject

class MeasuringRepositoryImpl @Inject constructor(
    private val measuringDao: MeasuringDao,
): MeasuringRepository {
    override suspend fun add(measuring: MeasuringDocument) {
        val fsMeasuring = measuring.toFSMeasuring()

        measuringDao.add(fsMeasuring)
    }

    override suspend fun measuringById(
        measuringId: String,
    ): ReadMeasuringRepositoryResponse = either {
        measuringDao.measuringById(measuringId)
            .map { it.toMeasuringDocument() }
            .mapLeft {
                when(it) {
                    is ReadMeasuringDaoError.NotFound ->
                        ReadMeasuringRepositoryError.NotFound(
                            description = it.description,
                            throwable = it.throwable,
                        )
                    is ReadMeasuringDaoError.Unexpected ->
                        ReadMeasuringRepositoryError.Unexpected(
                            description = it.description,
                            throwable = it.throwable,
                        )
                }
        }.bind()
    }

    override suspend fun updateMeasuring(
        measuringId: String,
        measuringUpdate: MeasuringUpdateDocument
    ): UpdateMeasuringRepositoryResponse = either {
        measuringDao.updateMeasuring(measuringId, measuringUpdate.toFSMeasuringUpdate())
            .mapLeft {
                UpdateMeasuringRepositoryError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()
    }
}
