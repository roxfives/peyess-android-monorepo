package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.data.repository.local_sale.frames.error.FramesDataNotFound
import com.peyess.salesapp.data.repository.local_sale.frames.error.Unexpected
import javax.inject.Inject

class LocalFramesRepositoryImpl @Inject constructor(
    private val framesDataDao: FramesDataDao,
): LocalFramesRepository {
    override suspend fun framesForServiceOrder(soId: String): LocalFramesRepositoryResponse =
        Either.catch {
            framesDataDao.getFramesForServiceOrder(soId)
        }.mapLeft {
            Unexpected(
                message = "Error while getting frames for service order $soId",
                error = it,
            )
        }.leftIfNull {
            FramesDataNotFound(
                message = "No frames data found for service order with id: $soId"
            )
        }
}
