package com.peyess.salesapp.data.repository.measuring

import arrow.core.Either
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.data.model.measuring.MeasuringUpdateDocument
import com.peyess.salesapp.data.repository.measuring.error.ReadMeasuringRepositoryError
import com.peyess.salesapp.data.repository.measuring.error.UpdateMeasuringRepositoryError

typealias ReadMeasuringRepositoryResponse = Either<ReadMeasuringRepositoryError, MeasuringDocument>

typealias UpdateMeasuringRepositoryResponse = Either<UpdateMeasuringRepositoryError, Unit>

interface MeasuringRepository {
    suspend fun add(measuring: MeasuringDocument)

    suspend fun measuringById(measuringId: String): ReadMeasuringRepositoryResponse

    suspend fun updateMeasuring(
        measuringId: String,
        measuringUpdate: MeasuringUpdateDocument,
    ): UpdateMeasuringRepositoryResponse
}
