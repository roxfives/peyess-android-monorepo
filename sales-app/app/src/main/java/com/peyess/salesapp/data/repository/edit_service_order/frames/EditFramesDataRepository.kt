package com.peyess.salesapp.data.repository.edit_service_order.frames

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.InsertFramesError
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.ReadFramesError
import com.peyess.salesapp.data.repository.edit_service_order.frames.error.UpdateFramesError
import com.peyess.salesapp.typing.frames.FramesType
import kotlinx.coroutines.flow.Flow

typealias EditFramesAddResponse = Either<InsertFramesError, Unit>
typealias EditFramesFetchResponse = Either<ReadFramesError, LocalFramesDocument>
typealias EditFramesStreamResponse = Flow<Either<ReadFramesError, LocalFramesDocument>>
typealias EditFramesUpdateResponse = Either<UpdateFramesError, Unit>

interface EditFramesDataRepository {
    suspend fun addFrame(frames: LocalFramesDocument): EditFramesAddResponse

    suspend fun findFramesForServiceOrder(serviceOrderId: String): EditFramesFetchResponse
    fun streamFramesForServiceOrder(serviceOrderId: String): EditFramesStreamResponse

    suspend fun updateIsNew(soId: String, isNew: Boolean): EditFramesUpdateResponse
    suspend fun updateDescription(soId: String, description: String): EditFramesUpdateResponse
    suspend fun updateReference(soId: String, reference: String): EditFramesUpdateResponse
    suspend fun updateValue(soId: String, value: Double): EditFramesUpdateResponse
    suspend fun updateTagCode(soId: String, tagCode: String): EditFramesUpdateResponse
    suspend fun updateType(soId: String, type: FramesType): EditFramesUpdateResponse
    suspend fun updateInfo(soId: String, info: String): EditFramesUpdateResponse
}