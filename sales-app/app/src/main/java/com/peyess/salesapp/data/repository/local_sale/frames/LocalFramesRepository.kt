package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.error.LocalFramesRepositoryError
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument

typealias UpdateFramesResponse = Either<LocalFramesRepositoryError, Unit>
typealias UpdateFramesNewResponse = Either<LocalFramesRepositoryError, Unit>
typealias LocalFramesRepositoryResponse = Either<LocalFramesRepositoryError, FramesDocument>

interface LocalFramesRepository {
    suspend fun updateFrames(frames: FramesDocument): UpdateFramesResponse

    suspend fun updateFramesNew(serviceOrderId: String, isNew: Boolean): UpdateFramesNewResponse

    suspend fun framesForServiceOrder(soId: String): LocalFramesRepositoryResponse
}