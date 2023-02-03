package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.data.repository.local_sale.frames.adapter.toFramesDocument
import com.peyess.salesapp.data.repository.local_sale.frames.adapter.toFramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.error.FramesDataNotFound
import com.peyess.salesapp.data.repository.local_sale.frames.error.Unexpected
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.feature.sale.payment.adapter.toFrames
import javax.inject.Inject

class LocalFramesRepositoryImpl @Inject constructor(
    private val framesDataDao: FramesDataDao,
): LocalFramesRepository {
    override suspend fun updateFrames(
        frames: FramesDocument,
    ): UpdateFramesResponse = Either.catch {
        framesDataDao.updateFrames(frames.toFramesEntity())
    }.mapLeft {
        Unexpected(
            description = "Error while updating frames for service order ${frames.soId}",
            error = it,
        )
    }

    override suspend fun updateFramesNew(
        serviceOrderId: String,
        isNew: Boolean
    ): UpdateFramesNewResponse = Either.catch {
        val newAsInt = if (isNew) 1 else 0

        framesDataDao.updateFramesNew(serviceOrderId, newAsInt)
    }.mapLeft {
        Unexpected(
            description = "Error while updating frames new for service order $serviceOrderId",
            error = it,
        )
    }

    override suspend fun framesForServiceOrder(soId: String): LocalFramesRepositoryResponse =
        Either.catch {
            framesDataDao
                .getFramesForServiceOrder(soId)
                ?.toFramesDocument()
        }.mapLeft {
            Unexpected(
                description = "Error while getting frames for service order $soId",
                error = it,
            )
        }.leftIfNull {
            FramesDataNotFound(
                description = "No frames data found for service order with id: $soId"
            )
        }
}
