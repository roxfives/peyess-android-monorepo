package com.peyess.salesapp.data.repository.measuring

import arrow.core.Either
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.data.repository.measuring.error.ReadMeasuringRepositoryError

typealias ReadMeasuringRepositoryResponse = Either<ReadMeasuringRepositoryError, MeasuringDocument>

interface MeasuringRepository {
    suspend fun add(measuring: MeasuringDocument)

    suspend fun measuringById(measuringId: String): ReadMeasuringRepositoryResponse
}
