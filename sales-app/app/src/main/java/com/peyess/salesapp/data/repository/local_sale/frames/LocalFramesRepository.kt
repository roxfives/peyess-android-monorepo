package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.error.LocalFramesRepositoryError

typealias LocalFramesRepositoryResponse = Either<LocalFramesRepositoryError, FramesEntity>

interface LocalFramesRepository {
    suspend fun framesForServiceOrder(soId: String): LocalFramesRepositoryResponse
}