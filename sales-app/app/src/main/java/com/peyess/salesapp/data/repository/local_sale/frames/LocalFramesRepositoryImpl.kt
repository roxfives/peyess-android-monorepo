package com.peyess.salesapp.data.repository.local_sale.frames

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.data.repository.local_sale.frames.adapter.toFramesDocument
import com.peyess.salesapp.data.repository.local_sale.frames.adapter.toFramesEntity
import com.peyess.salesapp.data.repository.local_sale.frames.error.FramesDataNotFound
import com.peyess.salesapp.data.repository.local_sale.frames.error.Unexpected
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import kotlinx.coroutines.flow.map
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

    override suspend fun framesForServiceOrder(serviceOrderId: String): LocalFramesRepositoryResponse =
        Either.catch {
            framesDataDao
                .getFramesForServiceOrder(serviceOrderId)
                ?.toFramesDocument()
        }.mapLeft {
            Unexpected(
                description = "Error while getting frames for service order $serviceOrderId",
                error = it,
            )
        }.leftIfNull {
            FramesDataNotFound(
                description = "No frames data found for service order with id: $serviceOrderId"
            )
        }

    override fun streamFramesForServiceOrder(
        serviceOrderId: String,
    ): LocalFramesStreamResponse {
        return framesDataDao.streamFramesForServiceOrder(serviceOrderId)
            .map { framesEntity ->
                if (framesEntity == null) {
                    FramesDataNotFound(
                        description = "No frames data found for service order with id: $serviceOrderId"
                    ).left()
                } else {
                    framesEntity.toFramesDocument().right()
                }
            }
    }
}
