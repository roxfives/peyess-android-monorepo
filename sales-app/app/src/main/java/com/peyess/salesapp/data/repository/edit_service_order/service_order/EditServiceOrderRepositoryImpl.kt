package com.peyess.salesapp.data.repository.edit_service_order.service_order

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.adapter.edit_service_order.service_order.toEditServiceOrderEntity
import com.peyess.salesapp.data.adapter.edit_service_order.service_order.toServiceOrderDocument
import com.peyess.salesapp.data.dao.edit_service_order.service_order.EditServiceOrderDao
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.DeleteServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.InsertServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.ReadServiceOrderError
import com.peyess.salesapp.data.repository.edit_service_order.service_order.error.UpdateServiceOrderError
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditServiceOrderRepositoryImpl @Inject constructor(
    private val serviceOrderDao: EditServiceOrderDao,
): EditServiceOrderRepository {
    override suspend fun addServiceOrder(
        serviceOrder: LocalServiceOrderDocument,
    ): EditServiceOrderInsertResponse = Either.catch {
        serviceOrderDao.addServiceOrder(serviceOrder.toEditServiceOrderEntity())
    }.mapLeft {
        InsertServiceOrderError.Unexpected(
            description = "Error adding service order $serviceOrder"
        )
    }

    override suspend fun serviceOrderById(
        serviceOrderId: String,
    ): EditServiceOrderFetchResponse = Either.catch {
        serviceOrderDao
            .serviceOrderById(serviceOrderId)
            ?.toServiceOrderDocument()
    }.mapLeft {
        ReadServiceOrderError.Unexpected(
            description = "Error fetching service order $serviceOrderId",
            throwable = it,
        )
    }.leftIfNull {
        ReadServiceOrderError.ServiceOrderNotFound(
            description = "Service order $serviceOrderId not found",
        )
    }

    override fun streamServiceOrderById(serviceOrderId: String): EditServiceOrderStreamResponse {
        return serviceOrderDao
            .streamServiceOrderById(serviceOrderId)
            .map {
                if (it != null) {
                    it.toServiceOrderDocument().right()
                } else {
                    ReadServiceOrderError.ServiceOrderNotFound(
                        description = "Error fetching service order $serviceOrderId"
                    ).left()
                }
            }
    }

    override suspend fun updateHasPrescription(
        id: String,
        hasPrescription: Boolean,
    ): EditServiceOrderUpdateResponse = Either.catch {
        val asInt = if (hasPrescription) 1 else 0

        serviceOrderDao.updateHasPrescription(id, asInt)
    }.mapLeft {
        UpdateServiceOrderError.Unexpected(
            description = "Error while updating service order $id with " +
                    "hasPrescription = $hasPrescription",
            throwable = it,
        )
    }

    override suspend fun updateClientName(
        id: String,
        clientName: String,
    ): EditServiceOrderUpdateResponse = Either.catch {
        serviceOrderDao.updateClientName(id, clientName)
    }.mapLeft {
        UpdateServiceOrderError.Unexpected(
            description = "Error while updating service order $id with " +
                    "clientName = $clientName",
            throwable = it,
        )
    }

    override suspend fun updateLensTypeCategoryName(
        id: String,
        lensTypeCategoryName: LensTypeCategoryName,
    ): EditServiceOrderUpdateResponse = Either.catch {
        serviceOrderDao.updateLensTypeCategoryName(id, lensTypeCategoryName)
    }.mapLeft {
        UpdateServiceOrderError.Unexpected(
            description = "Error while updating service order $id with " +
                    "lensTypeCategoryName = $lensTypeCategoryName",
            throwable = it,
        )
    }

    override suspend fun updateIsLensTypeMono(
        id: String,
        isLensTypeMono: Boolean,
    ): EditServiceOrderUpdateResponse = Either.catch {
        val asInt = if (isLensTypeMono) 1 else 0

        serviceOrderDao.updateIsLensTypeMono(id, asInt)
    }.mapLeft {
        UpdateServiceOrderError.Unexpected(
            description = "Error while updating service order $id with " +
                    "isLensTypeMono = $isLensTypeMono",
            throwable = it,
        )
    }

    override suspend fun deleteServiceOrderById(
        serviceOrderId: String,
    ): EditServiceOrderDeleteResponse = Either.catch {
        serviceOrderDao.deleteServiceOrderById(serviceOrderId)
    }.mapLeft {
        DeleteServiceOrderError.Unexpected(
            description = "Error while deleting service order $serviceOrderId",
            throwable = it,
        )
    }
}
