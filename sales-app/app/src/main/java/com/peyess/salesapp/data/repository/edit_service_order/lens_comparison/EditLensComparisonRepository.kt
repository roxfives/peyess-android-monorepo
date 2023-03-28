package com.peyess.salesapp.data.repository.edit_service_order.lens_comparison

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.DeleteLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.InsertLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.ReadLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.UpdateLensComparisonError
import kotlinx.coroutines.flow.Flow

typealias EditLensComparisonInsertResponse = Either<InsertLensComparisonError, Unit>
typealias EditLensComparisonUpdateResponse = Either<UpdateLensComparisonError, Unit>
typealias EditLensComparisonFetchResponse =
        Either<ReadLensComparisonError, List<LensComparisonDocument>>
typealias EditLensComparisonStreamResponse = Flow<EditLensComparisonFetchResponse>

typealias EditLensComparisonDeleteResponse = Either<DeleteLensComparisonError, Unit>

interface EditLensComparisonRepository {
    suspend fun addLensComparison(
        lensComparison: LensComparisonDocument,
    ): EditLensComparisonInsertResponse

    suspend fun updateLensComparison(
        lensComparison: LensComparisonDocument,
    ): EditLensComparisonUpdateResponse

    fun streamLensComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonStreamResponse

    fun deleteById(id: Int): EditLensComparisonDeleteResponse

    suspend fun deleteComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonDeleteResponse
}
