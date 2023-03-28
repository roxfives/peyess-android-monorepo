package com.peyess.salesapp.data.repository.edit_service_order.frames

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.frames.toEditFramesDataEntity
import com.peyess.salesapp.data.adapter.edit_service_order.frames.toLocalFramesDocument
import com.peyess.salesapp.data.dao.edit_service_order.frames.EditFramesDataDao
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.DeleteFramesError
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.InsertFramesError
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.ReadFramesError
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.UpdateFramesError
import com.peyess.salesapp.typing.frames.FramesType
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditFramesDataRepositoryImpl @Inject constructor(
    private val editFramesDataDao: EditFramesDataDao,
): EditFramesDataRepository {
    override suspend fun addFrame(
        frames: LocalFramesDocument,
    ): EditFramesAddResponse = Either.catch {
        val entity = frames.toEditFramesDataEntity()

        editFramesDataDao.addFrames(entity)
    }.mapLeft {
        InsertFramesError.Unexpected(
            description = "Error while adding frames to database: $it",
            throwable = it,
        )
    }

    override suspend fun findFramesForServiceOrder(
        serviceOrderId: String,
    ): EditFramesFetchResponse = Either.catch {
        editFramesDataDao
            .findFramesForServiceOrder(serviceOrderId)
            ?.toLocalFramesDocument()
    }.mapLeft {
        ReadFramesError.Unexpected(
            description = "Error while reading frames from database: $it",
            throwable = it,
        )
    }.leftIfNull {
        ReadFramesError.FramesNotFound(
            description = "No frames found for service order: $serviceOrderId",
        )
    }

    override fun streamFramesForServiceOrder(
        serviceOrderId: String,
    ): EditFramesStreamResponse {
        return editFramesDataDao.streamFramesForServiceOrder(serviceOrderId).map {
                if (it == null) {
                    ReadFramesError.FramesNotFound(
                        description = "Error while reading frames from " +
                                "database for service order: $serviceOrderId",
                    ).left()
                } else {
                    it.toLocalFramesDocument().right()
                }
            }.catch {
                ReadFramesError.Unexpected(
                    description = "Error while reading frames from database: $it",
                    throwable = it,
                ).left()
            }
    }

    override suspend fun updateIsNew(
        soId: String, isNew: Boolean,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateIsNew(soId, isNew)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $isNew"
        )
    }

    override suspend fun updateDescription(
        soId: String, description: String,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateDescription(soId, description)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $description"
        )
    }

    override suspend fun updateReference(
        soId: String, reference: String,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateReference(soId, reference)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $reference"
        )
    }

    override suspend fun updateValue(
        soId: String, value: Double,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateValue(soId, value)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $value"
        )
    }

    override suspend fun updateTagCode(
        soId: String, tagCode: String,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateTagCode(soId, tagCode)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $tagCode"
        )
    }

    override suspend fun updateType(
        soId: String, type: FramesType,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateType(soId, type)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $type"
        )
    }

    override suspend fun updateInfo(
        soId: String, info: String,
    ): EditFramesUpdateResponse = Either.catch {
        editFramesDataDao.updateInfo(soId, info)
    }.mapLeft {
        UpdateFramesError.Unexpected(
            description = "Error while updating frames for service order $soId with $info"
        )
    }

    override suspend fun deleteFramesForServiceOrder(
        serviceOrderId: String,
    ): EditFramesDeleteResponse = Either.catch {
        editFramesDataDao.deleteFramesForServiceOrder(serviceOrderId)
    }.mapLeft {
        DeleteFramesError.Unexpected(
            description = "Error while deleting frames for service order $serviceOrderId",
            throwable = it,
        )
    }
}
