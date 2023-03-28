package com.peyess.salesapp.data.repository.edit_service_order.product_picked

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.product_picked.toEditProductPicked
import com.peyess.salesapp.data.adapter.edit_service_order.product_picked.toProductPickedDocument
import com.peyess.salesapp.data.dao.edit_service_order.product_picked.EditProductPickedDao
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.DeleteProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.InsertProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.ReadProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.UpdateProductPickedError
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditProductPickedRepositoryImpl @Inject constructor(
    private val productPickedDao: EditProductPickedDao,
): EditProductPickedRepository {
    override suspend fun addProductPicked(
        productPicked: ProductPickedDocument,
    ): EditProductPickedInsertResponse = Either.catch {
        productPickedDao.addProductPicked(productPicked.toEditProductPicked())
    }.mapLeft {
        InsertProductPickedError.Unexpected(
            description = "Error while inserting product picked",
            throwable = it
        )
    }

    override suspend fun productPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedFetchResponse = Either.catch {
        productPickedDao
            .productPickedForServiceOrder(serviceOrderId)
            ?.toProductPickedDocument()
    }.mapLeft {
        ReadProductPickedError.Unexpected(
            description = "Error while inserting product picked",
            throwable = it
        )
    }.leftIfNull {
        ReadProductPickedError.ProductPickedNotFound(
            description = "Product picked not found for service order id: $serviceOrderId"
        )
    }

    override fun streamProductPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedStreamResponse {
        return productPickedDao.streamProductPickedForServiceOrder(serviceOrderId)
            .map {
                if (it != null) {
                    it.toProductPickedDocument().right()
                } else {
                    ReadProductPickedError.ProductPickedNotFound(
                        description = "Product picked not found for service order id: $serviceOrderId"
                    ).left()
                }
            }
    }

    override suspend fun updateLensId(
        serviceOrderId: String, lensId: String
    ): EditProductPickedUpdateResponse = Either.catch {
        productPickedDao.updateLensId(serviceOrderId, lensId)
    }.mapLeft {
        UpdateProductPickedError.Unexpected(
            description = "Error while updating product picked for " +
                    "service order $serviceOrderId with lensId = $lensId"
        )
    }

    override suspend fun updateColoringId(
        serviceOrderId: String, coloringId: String
    ): EditProductPickedUpdateResponse = Either.catch {
        productPickedDao.updateColoringId(serviceOrderId, coloringId)
    }.mapLeft {
        UpdateProductPickedError.Unexpected(
            description = "Error while updating product picked for " +
                    "service order $serviceOrderId with coloringId = $coloringId"
        )
    }

    override suspend fun updateTreatmentId(
        serviceOrderId: String, treatmentId: String
    ): EditProductPickedUpdateResponse = Either.catch {
        productPickedDao.updateTreatmentId(serviceOrderId, treatmentId)
    }.mapLeft {
        UpdateProductPickedError.Unexpected(
            description = "Error while updating product picked for " +
                    "service order $serviceOrderId with treatmentId = $treatmentId"
        )
    }

    override suspend fun deleteProductPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedDeleteResponse = Either.catch {
        productPickedDao.deleteProductPickedForServiceOrder(serviceOrderId)
    }.mapLeft {
        DeleteProductPickedError.Unexpected(
            description = "Error while deleting product picked for service order $serviceOrderId",
            throwable = it,
        )
    }
}
