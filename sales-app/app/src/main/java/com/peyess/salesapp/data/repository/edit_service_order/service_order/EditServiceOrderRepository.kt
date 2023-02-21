package com.peyess.salesapp.data.repository.edit_service_order.service_order

import arrow.core.Either
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.DeleteServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.InsertServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.ReadServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.UpdateServiceOrderError
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import kotlinx.coroutines.flow.Flow

typealias EditServiceOrderInsertResponse = Either<InsertServiceOrderError, Unit>

typealias EditServiceOrderFetchResponse = Either<ReadServiceOrderError, LocalServiceOrderDocument>
typealias EditServiceOrderStreamResponse = Flow<EditServiceOrderFetchResponse>

typealias EditServiceOrderUpdateResponse = Either<UpdateServiceOrderError, Unit>

typealias EditServiceOrderDeleteResponse = Either<DeleteServiceOrderError, Unit>

interface EditServiceOrderRepository {
    suspend fun addServiceOrder(serviceOrder: LocalServiceOrderDocument): EditServiceOrderInsertResponse

    suspend fun serviceOrderById(serviceOrderId: String): EditServiceOrderFetchResponse

    fun streamServiceOrderById(serviceOrderId: String): EditServiceOrderStreamResponse

    suspend fun updateHasPrescription(
        id: String,
        hasPrescription: Boolean,
    ): EditServiceOrderUpdateResponse

    suspend fun updateClientName(
        id: String,
        clientName: String,
    ): EditServiceOrderUpdateResponse

    suspend fun updateLensTypeCategoryName(
        id: String,
        lensTypeCategoryName: LensTypeCategoryName,
    ): EditServiceOrderUpdateResponse

    suspend fun updateIsLensTypeMono(
        id: String,
        isLensTypeMono: Boolean,
    ): EditServiceOrderUpdateResponse

    suspend fun deleteServiceOrderById(
        serviceOrderId: String,
    ): EditServiceOrderDeleteResponse
}
