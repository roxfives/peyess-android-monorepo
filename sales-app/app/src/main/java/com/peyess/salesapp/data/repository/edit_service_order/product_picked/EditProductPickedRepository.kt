package com.peyess.salesapp.data.repository.edit_service_order.product_picked

import arrow.core.Either
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.DeleteProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.InsertProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.ReadProductPickedError
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.error.UpdateProductPickedError
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import kotlinx.coroutines.flow.Flow

typealias EditProductPickedInsertResponse = Either<InsertProductPickedError, Unit>

typealias EditProductPickedFetchResponse = Either<ReadProductPickedError, ProductPickedDocument>
typealias EditProductPickedStreamResponse = Flow<EditProductPickedFetchResponse>

typealias EditProductPickedUpdateResponse = Either<UpdateProductPickedError, Unit>

typealias EditProductPickedDeleteResponse = Either<DeleteProductPickedError, Unit>

interface EditProductPickedRepository {
    suspend fun addProductPicked(
        productPicked: ProductPickedDocument,
    ): EditProductPickedInsertResponse

    suspend fun productPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedFetchResponse
    fun streamProductPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedStreamResponse

    suspend fun updateLensId(
        serviceOrderId: String,
        lensId: String,
    ): EditProductPickedUpdateResponse
    suspend fun updateColoringId(
        serviceOrderId: String,
        coloringId: String,
    ): EditProductPickedUpdateResponse
    suspend fun updateTreatmentId(
        serviceOrderId: String,
        treatmentId: String,
    ): EditProductPickedUpdateResponse

    suspend fun deleteProductPickedForServiceOrder(
        serviceOrderId: String,
    ): EditProductPickedDeleteResponse
}
