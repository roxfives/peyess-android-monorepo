package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import com.peyess.salesapp.data.repository.local_sale.frames.error.LocalFramesRepositoryError
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import kotlinx.coroutines.flow.Flow

typealias UpdateFramesResponse = Either<LocalFramesRepositoryError, Unit>
typealias UpdateFramesNewResponse = Either<LocalFramesRepositoryError, Unit>
typealias LocalFramesRepositoryResponse = Either<LocalFramesRepositoryError, FramesDocument>
typealias LocalFramesStreamResponse = Flow<Either<LocalFramesRepositoryError, FramesDocument>>

interface LocalFramesRepository {
    suspend fun updateFrames(frames: FramesDocument): UpdateFramesResponse

    suspend fun updateFramesNew(serviceOrderId: String, isNew: Boolean): UpdateFramesNewResponse

    suspend fun framesForServiceOrder(serviceOrderId: String): LocalFramesRepositoryResponse

    fun streamFramesForServiceOrder(serviceOrderId: String): LocalFramesStreamResponse

    suspend fun createFramesIfNotExists(serviceOrderId: String)
}